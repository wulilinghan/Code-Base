package top.b0x0.demo.io.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.apache.tools.tar.TarOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.b0x0.demo.io.util.oss.OSSUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 解压/压缩工具类
 * <p>
 * 压缩 .tar.gz 文件 {@link packetTarGz}
 * 解压 .tar.gz 文件 {@link unTarGz}
 * </p>
 *
 * @author TANG  压缩文件
 */
public class CompressFileUtils {

    private static final Logger log = LoggerFactory.getLogger(CompressFileUtils.class);

    public static final String GBK_ENCODING = "GBK";

    /**
     * 压缩 .tar.gz 文件
     *
     * @param resourceList 源文件集合
     * @param outPath      目标文件
     * @throws Exception 异常 压缩
     */
    public static void packetTarGz(List<File> resourceList, String outPath) throws Exception {
        // 参数验证，初始化输出路径
        if (resourceList == null || resourceList.size() < 1 || org.springframework.util.StringUtils.isEmpty(outPath)) {
            log.error("文件压缩执行异常, 非法参数!");
        }

        // 迭代源文件集合，将文件打包为 tar
        try (FileOutputStream fileOutputStream = new FileOutputStream(outPath + ".tmp");
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutputStream);
             TarOutputStream tarOutputStream = new TarOutputStream(bufferedOutput)) {
            for (File resourceFile : resourceList) {
                if (!resourceFile.isFile()) {
                    continue;
                }
                FileInputStream fileInputStream = new FileInputStream(resourceFile);
                BufferedInputStream bufferedInput = new BufferedInputStream(fileInputStream);
                TarEntry entry = new TarEntry(new File(resourceFile.getName()));
                entry.setSize(resourceFile.length());
                tarOutputStream.putNextEntry(entry);
                IOUtils.copy(bufferedInput, tarOutputStream);
                tarOutputStream.closeEntry();
            }
        } catch (Exception e) {
            Files.delete(Paths.get(outPath + ".tmp"));
            log.error("文件压缩至 {} 执行异常，嵌套异常！", outPath, e);
        }

        // 读取打包好的 tar 临时文件，使用 GZIP 方式压缩
        try (FileInputStream fileInputStream = new FileInputStream(outPath + ".tmp");
             BufferedInputStream bufferedInput = new BufferedInputStream(fileInputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(outPath);
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(gzipOutputStream);
        ) {
            byte[] cache = new byte[1024];
            for (int index = bufferedInput.read(cache); index != -1; index = bufferedInput.read(cache)) {
                bufferedOutput.write(cache, 0, index);
            }
            log.info("文件 {} 压缩执行完毕", outPath);
        } catch (Exception e) {
            log.error("文件压缩至 {} 执行异常，嵌套异常！", outPath, e);
        } finally {
            Files.delete(Paths.get(outPath + ".tmp"));
        }
    }

    /**
     * 解压 .tar.gz文件
     *
     * @param targzFile .tar.gz 压缩文件
     * @param outPath   存放解压后文件的目录
     */
    public static void unTarGz(File targzFile, String outPath) {
        // 验证参数，初始化输出路径
        if (targzFile == null || !targzFile.isFile() || org.springframework.util.StringUtils.isEmpty(outPath)) {
            log.error("文件解压缩执行异常，非法参数!");
        } else {
            // 读取 .tar.gz 文件转换为 tar 文件
            try (FileInputStream fileInputStream = new FileInputStream(targzFile);
                 BufferedInputStream bins = new BufferedInputStream(fileInputStream);
                 GZIPInputStream gzipInputStream = new GZIPInputStream(bins);
                 TarInputStream tarIn = new TarInputStream(gzipInputStream, 1024 * 2)) {
                // 迭代 tar 文件集合，解压文件
                for (TarEntry entry = tarIn.getNextEntry(); entry != null; entry = tarIn.getNextEntry()) {
                    File targetFileName = new File(outPath + "/" + entry.getName());
                    IOUtils.copy(tarIn, new FileOutputStream(targetFileName));
                }
                log.info("文件 {} 解压执行完毕", targzFile);
            } catch (Exception e) {
                log.error("{} 解压执行异常!", targzFile, e);
            }
        }
    }

    /**
     * 解压缩tar
     *
     * @param file /
     */
    public static String unTar(String file) {
        List<Map<String, Object>> lists = Lists.newArrayList();
        try (FileInputStream fis = new FileInputStream(new File(file));
             GZIPInputStream is = new GZIPInputStream(new BufferedInputStream(fis));
             ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream("tar", is);
             //考虑到编码格式
             InputStreamReader inr = new InputStreamReader(is, GBK_ENCODING);
             BufferedReader reader = new BufferedReader(inr)
        ) {
            TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();
            String lineTxt = null;
            while (entry != null) {
                String name = entry.getName();
                String[] nameSplit = name.split("_");
                String sn = nameSplit[1];
                log.info("name:{},sn:{}", name, sn);
                while ((lineTxt = reader.readLine()) != null) {
                    String[] keys = lineTxt.split("\t");
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("sn", sn);
                    for (String key : keys) {
                        String[] values = key.split("=");
                        if (!StringUtils.isEmpty(values[0])) {
                            map.put(values[0], values[1]);
                        }
                    }
                    lists.add(map);
                }
                entry = (TarArchiveEntry) in.getNextEntry();
            }
            return JSON.toJSONString(lists);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    /**
     * 解压.gz文件
     *
     * @param inFileName eg: "C:\\xdf\\2021-04-20\\billdetail1618886270422.txt.gz"
     */
    private static void unGz(String inFileName) {
        if (!"gz".equalsIgnoreCase(getExtension(inFileName))) {
            System.err.println("File name must have extension of \".gz\"");
            return;
        }
        try {
            System.out.println("Opening the compressed file.");
            GZIPInputStream in = new GZIPInputStream(new FileInputStream(inFileName));
            System.out.println("Open the output file.");
            String outFileName = getFileName(inFileName);
            FileOutputStream out = new FileOutputStream(outFileName);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            log.error("doUncompressGz error{}", e.getMessage());
        }
    }

    /**
     * Used to extract and return the extension of a given file.
     *
     * @param f Incoming file to get the extension of
     * @return <code>String</code> representing the extension of the incoming
     * file.
     */
    public static String getExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');
        if (i > 0 && i < f.length() - 1) {
            ext = f.substring(i + 1);
        }
        return ext;
    }


    /**
     * Used to extract the filename without its extension.
     *
     * @param f Incoming file to get the filename
     * @return <code>String</code> representing the filename without its
     * extension.
     */
    public static String getFileName(String f) {
        String fname = "";
        int i = f.lastIndexOf('.');
        if (i > 0 && i < f.length() - 1) {
            fname = f.substring(0, i);
        }
        return fname;
    }

    /**
     * 获取路径下的所有文件/文件夹
     *
     * @param directoryPath  需要遍历的文件夹路径
     * @param isAddDirectory 是否将子文件夹的路径也添加到list集合中
     * @return /
     */
    public static List<String> getAllFile(String directoryPath, boolean isAddDirectory) {
        List<String> list = new ArrayList<String>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                if (isAddDirectory) {
                    list.add(file.getAbsolutePath());
                }
                list.addAll(getAllFile(file.getAbsolutePath(), isAddDirectory));
            } else {
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }

    public static void closeQuietly(InputStream input) {
        closeQuietly((Closeable) input);
    }

    public static void closeQuietly(OutputStream output) {
        closeQuietly((Closeable) output);
    }

    /**
     * 关闭流
     *
     * @param closeable /
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            log.info("CompressFileUtils stream close error: {}", e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        String objectName = "xdf/2021-04-20/billdetail1618886270422.txt.gz";
        String filePrimevalName = objectName.substring(objectName.lastIndexOf("/") + 1);
        byte[] byteArray = OSSUtils.getObjectByteArray(objectName);
        File file = null;
        File parentFilePath = null;
        String tempFilePath = null;
        String os = System.getProperty("os.name");
        if (os != null) {
            log.info("os:{}", os);
            if (os.toLowerCase().startsWith("win")) {
                tempFilePath = "D:/temp/xdf/" + filePrimevalName;
            } else if (os.toLowerCase().startsWith("linux")) {
                tempFilePath = "/temp/xdf/" + filePrimevalName;
            }
        } else {
            log.error("服务器类型错误!");
        }
        System.out.println("filepath = " + tempFilePath);
        if (byteArray != null) {
            assert tempFilePath != null;
            file = new File(tempFilePath);
            if (!file.exists()) {
                String parent = file.getParent();
                parentFilePath = new File(parent);
                if (!parentFilePath.exists()) {
                    boolean mkdirs = parentFilePath.mkdirs();
                    if (!mkdirs) {
                        log.error("创建文件夹失败...");
                        return;
                    }
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteArray, 0, byteArray.length);
            fos.flush();
            fos.close();
        }
        // 解压源文件
        unGz(tempFilePath);
        // 删除源压缩文件 .gz
        assert tempFilePath != null;
        boolean deleteSrcFile = new File(tempFilePath).delete();
        log.info("删除源压缩文件:{}, 源文件名:{}", deleteSrcFile, objectName);
        assert parentFilePath != null;
        List<String> allFile = getAllFile(parentFilePath.getPath(), false);
        System.out.println("allFile = " + allFile);
        for (String f : allFile) {
            File txtFile = new File(f);
            // 创建FileInputStream类对象读取File
            FileInputStream fis = null;
            // 创建InputStreamReader对象接收文件流
            InputStreamReader isr = null;
            // 创建reader缓冲区将文件流装进去
            BufferedReader br = null;
            try {
                fis = new FileInputStream(txtFile);
                isr = new InputStreamReader(fis);
                br = new BufferedReader(isr);
                String lineTxt = null;
                // 从缓冲区中逐行读取代码，调用readLine()方法
                while ((lineTxt = br.readLine()) != null) {
                    // 逐行输出文件内容
                    System.out.println(lineTxt);
                    // TODO解析内容
                    String[] x = lineTxt.split("\\|");
                    System.err.println(Arrays.toString(x));
                }
                boolean deleteTxtFile = txtFile.delete();
                log.info("文件读取完毕, 文件名:{}, 删除完毕{}", txtFile.getName(), deleteTxtFile);
            } catch (IOException e) {
                log.error("读取txt文件异常:{}", e.getMessage());
            } finally {
                closeQuietly(br);
                closeQuietly(isr);
                closeQuietly(fis);
            }
        }
    }
}
