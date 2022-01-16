package top.b0x0.demo.io.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.apache.tools.tar.TarOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.b0x0.demo.io.utils.oss.OSSUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.*;

/**
 * 文件解压/压缩工具类
 *
 * @author TANG
 * @since 2021/04/20
 */
public class CompressUtils {
    private static final Logger log = LoggerFactory.getLogger(CompressUtils.class);

    public static final String GBK_ENCODING = "GBK";
    private static final int BUFFER_SIZE = 1024 * 10;

    private static final String SYS_OS;
    private static boolean IS_WIM = false;
    private static boolean IS_LINUX = false;

    static {
        SYS_OS = System.getProperty("os.name");
        if (SYS_OS.toLowerCase().startsWith("win")) {
            IS_WIM = true;
        } else {
            IS_LINUX = true;
        }
    }

    // --------------------------------------------- 压缩文件方法 ------------------------------------------------------

    /**
     * 压缩 .tar.gz 文件
     *
     * @param srcFileList 源文件集合
     * @param outFile     目标文件 eg:d:/tmp/targz/out.tar.gz
     * @throws Exception /
     */
    public static void doTarGz(List<File> srcFileList, String outFile) throws Exception {
        // 参数验证，初始化输出路径
        if (srcFileList == null || srcFileList.size() < 1 || !org.springframework.util.StringUtils.hasText(outFile)) {
            log.error("文件压缩执行异常, 非法参数!");
            return;
        }
        File parentFile = new File(outFile).getParentFile();
        if (!parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
            if (!mkdirs) {
                log.error("创建文件夹失败.");
                return;
            }
        }
        // 迭代源文件集合，将文件打包为 tar
        try (FileOutputStream fileOutputStream = new FileOutputStream(outFile + ".tmp");
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutputStream);
             TarOutputStream tarOutputStream = new TarOutputStream(bufferedOutput)) {
            for (File resourceFile : srcFileList) {
                if (!resourceFile.isFile()) {
                    log.warn("该文件是文件夹, 跳过打包.");
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
        } catch (IOException e) {
            Files.delete(Paths.get(outFile + ".tmp"));
            log.error("文件压缩至 {} 执行异常，嵌套异常！", outFile, e);
        }

        // 读取打包好的 tar 临时文件，使用 GZIP 方式压缩
        try (FileInputStream fileInputStream = new FileInputStream(outFile + ".tmp");
             BufferedInputStream bufferedInput = new BufferedInputStream(fileInputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(outFile);
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(gzipOutputStream);
        ) {
            byte[] cache = new byte[1024];
            for (int index = bufferedInput.read(cache); index != -1; index = bufferedInput.read(cache)) {
                bufferedOutput.write(cache, 0, index);
            }
            log.info("文件 {} 压缩执行完毕", outFile);
        } catch (IOException e) {
            log.error("文件压缩至 {} 执行异常，嵌套异常！", outFile, e);
        } finally {
            Files.delete(Paths.get(outFile + ".tmp"));
        }
    }

    /**
     * 私有函数将文件集合压缩成tar包后返回
     *
     * @param srcFileList 要压缩的文件集合
     * @param outFile     tar 输出流的目标文件
     * @return File  指定返回的目标文件
     */
    public static File doTar(List<File> srcFileList, File outFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            try (BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE)) {
                try (TarArchiveOutputStream taos = new TarArchiveOutputStream(bos)) {
                    //解决文件名过长问题
                    taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
                    for (File file : srcFileList) {
                        taos.putArchiveEntry(new TarArchiveEntry(file));
                        try (FileInputStream fis = new FileInputStream(file)) {
                            IOUtils.copy(fis, taos);
                            taos.closeArchiveEntry();
                        }
                    }
                }
            }
        }
        return outFile;
    }

    /**
     * 将多个远端文件读取并压缩
     *
     * @param filesMap 键值对<文件名：文件链接>
     * @param outFile  /
     */
    public static void doZip4Url(Map<String, String> filesMap, String outFile) throws IOException, ServletException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(outFile)));
        Set<Map.Entry<String, String>> entrySet = filesMap.entrySet();
        for (Map.Entry<String, String> file : entrySet) {
            zipFile(getIsFromUrl(file.getValue()), file.getKey(), zipOutputStream);
        }
        zipOutputStream.close();
    }

    /**
     * 将本地某个文件/文件夹压缩
     * 注意: 源文件或目录与目标压缩文件路径请不要选同一个目录下,否则会死循环
     *
     * @param srcFileList 原文件或者目录的list  eg: [d:/test,d:/xxx.doc]
     * @param outFile     压缩后目标文件  eg: D:/testout/out.zip
     * @throws IOException /
     */
    public static void doZip(List<File> srcFileList, File outFile) throws IOException {
        checkSrcFileAndTargetFile(srcFileList, outFile);
        if (!outFile.getParentFile().exists()) {
            boolean mkdirs = outFile.getParentFile().mkdirs();
            if (!mkdirs) {
                log.error("目标路径未创建:{}", outFile.getParentFile());
                throw new IOException("目标路径未创建！");
            }
        }
        try (ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(outFile))) {
            for (File file : srcFileList) {
                addZipEntry("", file, outputStream);
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * 检查源文件与目标文件是否在同一目录下
     *
     * @param srcFileList   /
     * @param targetZipFile /
     */
    private static void checkSrcFileAndTargetFile(List<File> srcFileList, File targetZipFile) throws IOException {
        if (IS_WIM) {
            String[] targetFileSplit = targetZipFile.getParentFile().getPath().split("\\\\");
            for (File file : srcFileList) {
                if (!file.isDirectory()) {
                    continue;
                }
                String[] split = file.getPath().split("\\\\");
                if (split.length >= 2) {
                    if (split[1].equals(targetFileSplit[1])) {
                        throw new IOException("Windows源文件目录与目标文件路径冲突");
                    }
                } else {
                    throw new IOException("Windows源文件目录是系统根目录");
                }
            }
        } else {
            String[] targetFileSplit = targetZipFile.getParentFile().getPath().split("/");
            for (File file : srcFileList) {
                String[] split = file.getPath().split("/");
                if (split.length >= 2) {
                    if (split[1].equals(targetFileSplit[1])) {
                        throw new IOException("Linux源文件目录与目标文件路径冲突");
                    }
                } else {
                    throw new IOException("Linux源文件目录是系统根目录");
                }
            }
        }
    }

    /**
     * 将文件写入到zip文件中
     *
     * @param inputStream     /
     * @param fileName        /
     * @param zipOutputStream /
     * @throws IOException      /
     * @throws ServletException /
     */
    public static void zipFile(InputStream inputStream, String fileName, ZipOutputStream zipOutputStream) throws IOException, ServletException {
        if (inputStream != null) {
            BufferedInputStream bInStream = new BufferedInputStream(inputStream);
            ZipEntry entry = new ZipEntry(fileName);
            zipOutputStream.putNextEntry(entry);
            int len = 0;
            byte[] buffer = new byte[10 * 1024];
            while ((len = inputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
                zipOutputStream.flush();
            }
            // Closes the current ZIP entry and positions the stream for writing the next entry
            zipOutputStream.closeEntry();
            bInStream.close();
            inputStream.close();
        } else {
            throw new IOException("文件不存在！");
        }
    }

    /**
     * 通过网路文件地址获取文件流
     *
     * @param imgUrl /
     */
    public static InputStream getIsFromUrl(String imgUrl) throws IOException {
        //new一个URL对象
        URL url = new URL(imgUrl);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        return conn.getInputStream();
    }


    /**
     * 将文件写入到zip文件中
     *
     * @param baseName        /
     * @param srcFile         /
     * @param zipOutputStream /
     */
    private static void addZipEntry(String baseName, File srcFile, ZipOutputStream zipOutputStream) throws IOException {
        FileInputStream is = null;
        String entry = baseName + srcFile.getName();
        if (srcFile.isDirectory()) {
            File[] files = srcFile.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    // 递归导入文件
                    addZipEntry(entry + File.separator, file, zipOutputStream);
                }
            }
        } else {

            is = org.apache.commons.io.FileUtils.openInputStream(srcFile);
            zipOutputStream.putNextEntry(new ZipEntry(entry));

            int len = 0;
            byte[] buffer = new byte[2 * 1024];
            while ((len = is.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
                zipOutputStream.flush();
            }
            zipOutputStream.closeEntry();
        }
        if (is != null) {
            is.close();
        }
    }

    /**
     * 下载文件
     *
     * @param file     /
     * @param response /
     */
    public static void downloadFile(File file, HttpServletResponse response) throws IOException {
        // 用缓冲流下载文件。
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
        byte[] buffer = new byte[fis.available()];
        int read = fis.read(buffer);
        System.out.println("read = " + read);
        fis.close();
        // 清空response
        response.reset();
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((file.getName()).getBytes(), StandardCharsets.ISO_8859_1));
        os.write(buffer);
        os.flush();
        os.close();
    }

    // --------------------------------------------- 解压文件方法 ------------------------------------------------------

    /**
     * unzip the compressed file.
     * <pre>
     *     1. .zip
     *     2. .tar.gz
     *     3. .tgz
     *     4. .tar.bz2
     * </pre>
     *
     * @param srcFile   eg:d:\tmp\targz\out.tar.gz
     * @param outputDir eg:d:\tmp\targz
     * @param isDeleted true or false
     * @return boolean /
     */
    public static boolean decompress(String srcFile, String outputDir, boolean isDeleted) {
        File file = new File(srcFile);
        if (!file.exists()) {
            log.error("目标压缩文件不存在");
            return false;
        }
        if (!new File(outputDir).isDirectory()) {
            log.error("outputDir参数不是文件夹");
            return false;
        }
        try {
            if (srcFile.endsWith(".zip")) {
                unZip(file, outputDir);
            } else if (srcFile.endsWith(".tar.gz") || srcFile.endsWith(".tgz")) {
                unTarGz(file, outputDir);
            } else if (srcFile.endsWith(".tar.bz2")) {
                unTarBz2(file, outputDir);
            } else {
                log.warn("暂不支持此压缩类型文件:{}", srcFile);
                return false;
            }
            filterFile(new File(outputDir));
            if (isDeleted) {
                org.apache.commons.io.FileUtils.forceDelete(file);
            }
            return true;
        } catch (IOException e) {
            log.error("解压文件异常", e);
        }
        return false;
    }

    /**
     * 解压缩tar.bz2文件
     *
     * @param file      压缩包文件
     * @param outputDir 目标文件夹
     */
    public static void unTarBz2(File file, String outputDir) throws IOException {
        try (TarArchiveInputStream tarIn =
                     new TarArchiveInputStream(
                             new BZip2CompressorInputStream(
                                     new FileInputStream(file)))) {
            createDirectory(outputDir, null);
            TarArchiveEntry entry;
            while ((entry = tarIn.getNextTarEntry()) != null) {
                if (entry.isDirectory()) {
                    createDirectory(outputDir, entry.getName());
                } else {
                    File writeFile = new File(outputDir + File.separator + entry.getName());
                    if (!writeFile.getParentFile().exists()) {
                        if (!writeFile.getParentFile().mkdirs()) {
                            log.error("目标路径创建失败:{}", writeFile.getPath());
                        }
                    }
                    try (OutputStream out = new FileOutputStream(writeFile)) {
                        writeFile(tarIn, out);
                    }
                }
            }
        }
    }

    public static void unZip(File file, String outputDir) throws IOException {
        try (ZipFile zipFile = new ZipFile(file, StandardCharsets.UTF_8)) {
            //创建输出目录
            createDirectory(outputDir, null);
            Enumeration<?> enums = zipFile.entries();
            while (enums.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) enums.nextElement();
                if (entry.isDirectory()) {
                    //创建空目录
                    createDirectory(outputDir, entry.getName());
                } else {
                    try (InputStream in = zipFile.getInputStream(entry)) {
                        File writeFile = new File(outputDir + File.separator + entry.getName());
                        if (!writeFile.getParentFile().exists()) {
                            if (!writeFile.getParentFile().mkdirs()) {
                                log.error("目标路径创建失败:{}", writeFile.getPath());
                            }
                        }
                        try (OutputStream out = new FileOutputStream(
                                writeFile)) {
                            writeFile(in, out);
                        }
                    }
                }
            }
        }
    }

    public static void unTarGz(File tarGzFile, String outputDir) {
        // 验证参数，初始化输出路径
        if (tarGzFile == null || !tarGzFile.isFile() || !org.springframework.util.StringUtils.hasText(outputDir)) {
            log.error("文件解压缩执行异常，非法参数!");
        } else {
            // 读取 .tar.gz 文件转换为 tar 文件
            try (FileInputStream fileInputStream = new FileInputStream(tarGzFile);
                 BufferedInputStream bins = new BufferedInputStream(fileInputStream);
                 GZIPInputStream gzipInputStream = new GZIPInputStream(bins);
                 TarInputStream tarIn = new TarInputStream(gzipInputStream, 1024 * 2)) {
                // 迭代 tar 文件集合，解压文件
                for (TarEntry entry = tarIn.getNextEntry(); entry != null; entry = tarIn.getNextEntry()) {
                    File targetFileName = new File(outputDir + "/" + entry.getName());
                    IOUtils.copy(tarIn, new FileOutputStream(targetFileName));
                }
                log.info("文件 {} 解压执行完毕", tarGzFile);
            } catch (Exception e) {
                log.error("{} 解压执行异常!", tarGzFile, e);
            }
        }
    }

    /**
     * 解压缩tar
     *
     * @param srcFile /
     */
    public static String unTar(String srcFile) {
        List<Map<String, Object>> lists = Lists.newArrayList();
        try (FileInputStream fis = new FileInputStream(new File(srcFile));
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
     * 写文件
     *
     * @param in  /
     * @param out /
     * @throws IOException /
     */
    public static void writeFile(InputStream in, OutputStream out) throws IOException {
        int length = 0;
        byte[] b = new byte[BUFFER_SIZE];
        while ((length = in.read(b)) != -1) {
            out.write(b, 0, length);
            out.flush();
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
     * 创建目录
     *
     * @param outputDir /
     * @param subDir    /
     */
    public static void createDirectory(String outputDir, String subDir) {
        File file = new File(outputDir);
        //子目录不为空
        if (!(subDir == null || subDir.trim().equals(""))) {
            file = new File(outputDir + File.separator + subDir);
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.mkdirs();
        }
    }

    /**
     * 删除Mac压缩再解压产生的 __MACOSX 文件夹和 .开头的其他文件
     *
     * @param filteredFile /
     */
    public static void filterFile(File filteredFile) throws IOException {
        if (filteredFile != null) {
            File[] files = filteredFile.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.getName().startsWith(".") ||
                        (file.isDirectory() && "__MACOSX".equals(file.getName()))) {
                    org.apache.commons.io.FileUtils.forceDelete(file);
                }
            }
        }
    }


    public static byte[] getContent(File file) {
        try {
            return getContent(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    public static byte[] getContent(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            //byte[] buffer = new byte[16 * 1024];
            while (true) {
                int len = is.read(buffer);
                if (len == -1) {
                    break;
                }
                baos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
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
