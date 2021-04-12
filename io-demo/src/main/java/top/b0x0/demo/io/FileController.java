package top.b0x0.demo.io;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author TANG
 * @since 2021/04/12
 */
@Slf4j
@RestController
public class FileController {
    /**
     * 将磁盘的多个文件打包成压缩包并输出流下载
     */
    @GetMapping(value = "/d1")
    public void download1(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String outputFileName = "文件" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip";
        // 设置response参数
        response.reset();
        response.setContentType("content-type:octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((outputFileName).getBytes(), StandardCharsets.ISO_8859_1));
        ServletOutputStream out = response.getOutputStream();

        ZipArchiveOutputStream zous = new ZipArchiveOutputStream(out);
        zous.setUseZip64(Zip64Mode.AsNeeded);
        ClassPathResource resource1 = new ClassPathResource("images/2021二零二一牛年新春快乐4k高清壁纸_彼岸图网.jpg");
        ClassPathResource resource2 = new ClassPathResource("images/test.jpg");
        File f1 = resource1.getFile();
        File f2 = resource2.getFile();
        List<File> fileList = new ArrayList<>();
        fileList.add(f1);
        fileList.add(f2);

        for (File file : fileList) {
            String fileName = file.getName();
            InputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            byte[] bytes = baos.toByteArray();

            //设置文件名
            ArchiveEntry entry = new ZipArchiveEntry(fileName);
            zous.putArchiveEntry(entry);
            zous.write(bytes);
            zous.closeArchiveEntry();
            baos.close();
        }
        zous.close();
    }

    /**
     * 将网络url资源文件的多个文件打包成压缩包并输出流下载
     */
    @GetMapping(value = "/d2")
    public void download2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String outputFileName = "文件" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip";
        // 设置response参数
        response.reset();
        response.setContentType("content-type:octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((outputFileName).getBytes(), StandardCharsets.ISO_8859_1));
        ServletOutputStream out = response.getOutputStream();

        ZipArchiveOutputStream zous = new ZipArchiveOutputStream(out);
        zous.setUseZip64(Zip64Mode.AsNeeded);

        String path1 = "http://www.baidu.com/img/bd_logo1.png";
        String path2 = "http://www.baidu.com/img/bd_logo1.png";
        List<String> pathList = new ArrayList<>();
        pathList.add(path1);
        pathList.add(path2);

        for (String path : pathList) {
            String fileName = UUID.randomUUID() + ".png";
            InputStream inputStream = getInputStreamFromUrl(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            byte[] bytes = baos.toByteArray();

            //设置文件名
            ArchiveEntry entry = new ZipArchiveEntry(fileName);
            zous.putArchiveEntry(entry);
            zous.write(bytes);
            zous.closeArchiveEntry();
            baos.close();
        }
        zous.close();
    }

    /**
     * 通过网络地址获取文件InputStream
     *
     * @param path 地址
     * @return InputStream
     */
    public static InputStream getInputStreamFromUrl(String path) {
        URL url = null;
        InputStream is = null;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            log.error("getInputStreamFromUrl -> cast (URL) error:{}", e.getMessage());
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) Objects.requireNonNull(url).openConnection();
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
        } catch (IOException e) {
            log.error("getInputStreamFromUrl -> cast (HttpURLConnection) error:{}", e.getMessage());
        }
        return is;
    }


    /**
     * 批量下载文件时，需要将多个文件打包为zip，然后再下载。实现思路有两种：一是将所有文件先打包压缩为一个文件，然后下载这个压缩包，二是一边压缩一边下载，将多个文件逐一写入到压缩文件中。
     *
     * @param request  /
     * @param response /
     */
    @GetMapping(value = "/d3")
    public void downloadFiles(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //响应头的设置
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");

        //设置压缩包的名字
        //解决不同浏览器压缩包名字含有中文时乱码的问题
        String downloadName = "文件" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip";

        String agent = request.getHeader("USER-AGENT");
        try {
            if (agent.contains("MSIE") || agent.contains("Trident")) {
                downloadName = java.net.URLEncoder.encode(downloadName, "UTF-8");
            } else {
                downloadName = new String(downloadName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + downloadName + "\"");

        //设置压缩流：直接写入response，实现边压缩边下载
        ZipOutputStream zipos = null;
        try {
            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            //设置压缩方法
            zipos.setMethod(ZipOutputStream.DEFLATED);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassPathResource resource1 = new ClassPathResource("images/2021二零二一牛年新春快乐4k高清壁纸_彼岸图网.jpg");
        ClassPathResource resource2 = new ClassPathResource("images/test.jpg");
        File f1 = resource1.getFile();
        File f2 = resource2.getFile();
        List<File> fileList = new ArrayList<>();
        fileList.add(f1);
        fileList.add(f2);

        //循环将文件写入压缩流
        DataOutputStream os = null;
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            try {
                //添加ZipEntry，并ZipEntry中写入文件流
                //这里，加上i是防止要下载的文件有重名的导致下载失败
                Objects.requireNonNull(zipos).putNextEntry(new ZipEntry(i + file.getName()));
                os = new DataOutputStream(zipos);
                InputStream is = new FileInputStream(file);
                byte[] b = new byte[100];
                int length = 0;
                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }
                is.close();
                zipos.closeEntry();
            } catch (IOException e) {
                log.error("compressed file error:{}", e.getMessage());
            }
        }

        //关闭流
        try {
            if (os != null) {
                os.flush();
            }
            if (os != null) {
                os.close();
            }
            if (zipos != null) {
                zipos.close();
            }
        } catch (IOException e) {
            log.error("stream close error:{}", e.getMessage());
        }

    }
}
