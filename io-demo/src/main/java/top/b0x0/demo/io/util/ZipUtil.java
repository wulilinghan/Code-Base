package top.b0x0.demo.io.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 1. 将多个远端文件读取并压缩
 * 2. 将本地某个文件/文件夹压缩
 *
 * @author musui
 */
@Slf4j
public class ZipUtil {

    private ZipUtil() {
        throw new IllegalStateException("Utility class");
    }

//    public static void main(String[] args) {
//
//        try {
//            ZipUtil.zipPersonPhotoFile(map, new ZipOutputStream(new FileOutputStream(new File(zipFilePath))));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 压缩文件
     *
     * @param files           键值对<文件名：文件链接>
     * @param zipOutputStream /
     */
    public static void zipPersonPhotoFile(Map<String, String> files, ZipOutputStream zipOutputStream) {
        try {
            Set<Entry<String, String>> entrySet = files.entrySet();
            for (Entry<String, String> file : entrySet) {
                try {
                    zipFile(getImgIs(file.getValue()), file.getKey(), zipOutputStream);
                } catch (Exception e) {
                    log.error("zipPersonPhotoFile->zipFile method error:{}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("zipPersonPhotoFile method error:{}", e.getMessage());
        } finally {
            try {
                zipOutputStream.close();
            } catch (IOException e) {
                log.error("zipOutputStream close error:{}", e.getMessage());
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
        try {
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
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 获取文件流
     *
     * @param imgUrl /
     */
    public static InputStream getImgIs(String imgUrl) throws IOException {
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
     * 下载打包的文件
     *
     * @param file     /
     * @param response /
     */
    public static void downloadZip(File file, HttpServletResponse response) {
        try {
            // 以流的形式下载文件。
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            file.delete();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 将文件/目录进行压缩
     *
     * @param sourceFile    原文件/目录
     * @param targetZipFile 压缩后目标文件
     * @throws IOException /
     */
    public static void zipFiles(File sourceFile, File targetZipFile) throws IOException {
        ZipOutputStream outputStream = null;
        try {
            outputStream = new ZipOutputStream(new FileOutputStream(targetZipFile));
            addEntry("", sourceFile, outputStream);
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            outputStream.close();
        }
    }

    /**
     * 将文件写入到zip文件中
     *
     * @param source          /
     * @param zipOutputStream /
     */
    private static void addEntry(String base, File source, ZipOutputStream zipOutputStream)
            throws IOException, ServletException {
        FileInputStream is = null;
        try {
            String entry = base + source.getName();
            if (source.isDirectory()) {
                for (File file : source.listFiles()) {
                    // 递归导入文件
                    addEntry(entry + File.separator, file, zipOutputStream);
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