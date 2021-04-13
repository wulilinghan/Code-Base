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
 * 1. 将多个远端文件读取并压缩
 * 2. 将本地某个文件/文件夹压缩
 *
 * @author musui
 */
@Slf4j
public class ZipUtils {

    private static final String PATH_1 = "http://www.baidu.com/img/bd_logo1.png";
    private static final String PATH_2 = "http://www.baidu.com/img/bd_logo1.png";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final String ZIPFILE_OUT_PATH = "d:/test/" + SIMPLE_DATE_FORMAT.format(new Date()) + ".zip";


    public static void main(String[] args) throws IOException {

/*
        Map<String, String> fileMap = new HashMap<>(2);
        fileMap.put("logo1.png", PATH_1);
        fileMap.put("logo2.png", PATH_2);

        zipUrlFile(fileMap, new ZipOutputStream(new FileOutputStream(new File(ZIPFILE_OUT_PATH))));
*/

        String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("images/20210413160128024.zip")).getPath();
        System.out.println("path = " + path);

    }

    /**
     * 压缩远程文件
     *
     * @param files           键值对<文件名：文件链接>
     * @param zipOutputStream /
     */
    public static void zipUrlFile(Map<String, String> files, ZipOutputStream zipOutputStream) throws IOException, ServletException {
        Set<Entry<String, String>> entrySet = files.entrySet();
        for (Entry<String, String> file : entrySet) {
            zipFile(getIsFromUrl(file.getValue()), file.getKey(), zipOutputStream);
        }
        zipOutputStream.close();
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
            //Closes the current ZIP entry and positions the stream for writing the next entry
            zipOutputStream.closeEntry();
            bInStream.close();
            inputStream.close();
        } else {
            throw new ServletException("文件不存在！");
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

    /**
     * 将文件/目录进行压缩
     *
     * @param sourceFile    原文件/目录
     * @param targetZipFile 压缩后目标文件
     * @throws IOException /
     */
    public static void zipFiles(File sourceFile, File targetZipFile) throws IOException {
        try (ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(targetZipFile))) {
            addZipEntry("", sourceFile, outputStream);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * 将文件写入到zip文件中
     *
     * @param source          /
     * @param zipOutputStream /
     */
    private static void addZipEntry(String base, File source, ZipOutputStream zipOutputStream) throws IOException {
        FileInputStream is = null;
        try {
            String entry = base + source.getName();
            if (source.isDirectory()) {
                for (File file : source.listFiles()) {
                    // 递归导入文件
                    addZipEntry(entry + File.separator, file, zipOutputStream);
                }
            } else {

                is = FileUtils.openInputStream(source);
                if (is != null) {
                    zipOutputStream.putNextEntry(new ZipEntry(entry));

                    int len = 0;
                    byte[] buffer = new byte[10 * 1024];
                    while ((len = is.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, len);
                        zipOutputStream.flush();
                    }
                    zipOutputStream.closeEntry();
                }
            }

        } catch (IOException e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }

    }

}