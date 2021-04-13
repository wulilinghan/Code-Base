package top.b0x0.demo.io.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 1. 将多个远端文件读取并压缩  zipUrlFiles()
 * 2. 将本地某个文件/文件夹压缩 zipFiles()
 *
 * @author musui
 */
@Slf4j
public class ZipUtils {

    private static final String PATH_1 = "http://www.baidu.com/img/bd_logo1.png";
    private static final String PATH_2 = "http://www.baidu.com/img/bd_logo1.png";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final String ZIPFILE_OUT_PATH = "d:/test/" + SIMPLE_DATE_FORMAT.format(new Date()) + ".zip";
    private static final String SYS_OS;
    private static boolean SYS_OS_IS_WIM = false;
    private static boolean SYS_OS_IS_LINUX = false;

    static {
        SYS_OS = System.getProperty("os.name");
        if (SYS_OS.toLowerCase().startsWith("win")) {
            SYS_OS_IS_WIM = true;
        } else {
            SYS_OS_IS_LINUX = true;
        }
    }

    public static void main(String[] args) throws IOException, ServletException {
        System.out.println("os.name " + System.getProperty("os.name"));
        boolean win = SYS_OS.toLowerCase().startsWith("win");
        System.out.println("win = " + win);

        Map<String, String> fileMap = new HashMap<>(2);
        fileMap.put("logo1.png", PATH_1);
        fileMap.put("logo2.png", PATH_2);

        zipUrlFiles(fileMap, new ZipOutputStream(new FileOutputStream(new File(ZIPFILE_OUT_PATH))));

/*
        String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("images/20210413160128024.zip")).getPath();
        System.out.println("path = " + path);
*/
        File srcFile1 = new File("D:/test");
        File srcFile2 = new File("D:/Java开发手册（嵩山版）.pdf");
//        File srcFile4 = new File("D:");
//        File srcFile3 = new File("D:\\test\\temp\\gulimall-learning-master_20210227_111613012.zip");

        File targetFile = new File("D:/test2/zip-test/out-zip/out" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".zip");

        List<File> fileList = new ArrayList<>();
        fileList.add(srcFile1);
        fileList.add(srcFile2);
//        fileList.add(srcFile4);
        zipFiles(fileList, targetFile);

    }

    /**
     * 压缩远程文件
     *
     * @param filesMap           键值对<文件名：文件链接>
     * @param zipOutputStream /
     */
    public static void zipUrlFiles(Map<String, String> filesMap, ZipOutputStream zipOutputStream) throws IOException, ServletException {
        Set<Entry<String, String>> entrySet = filesMap.entrySet();
        for (Entry<String, String> file : entrySet) {
            zipFile(getIsFromUrl(file.getValue()), file.getKey(), zipOutputStream);
        }
        zipOutputStream.close();
    }

    /**
     * 将文件/目录进行压缩
     * 注意: 源文件或目录与目标压缩文件路径请不要选同一个目录下,否则会死循环
     *
     * @param srcFileList   原文件或者目录的list  eg: [d:/test,d:/xxx.doc]
     * @param targetZipFile 压缩后目标文件  eg: D:/testout/out.zip
     * @throws IOException /
     */
    public static void zipFiles(List<File> srcFileList, File targetZipFile) throws IOException {
        checkSrcFileAndTargetFile(srcFileList, targetZipFile);
        if (!targetZipFile.getParentFile().exists()) {
            boolean mkdirs = targetZipFile.getParentFile().mkdirs();
            if (!mkdirs) {
                throw new IOException("目标路径未创建！");
            }
        }
        try (ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(targetZipFile))) {
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
        if (SYS_OS_IS_WIM) {
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

            is = FileUtils.openInputStream(srcFile);
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

}