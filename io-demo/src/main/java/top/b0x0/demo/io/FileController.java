package top.b0x0.demo.io;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.demo.io.util.ZipUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
        String outputFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip";
        response.reset();
        response.setContentType("application/octet-stream");
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
        String outputFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip";
        response.reset();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((outputFileName).getBytes(), StandardCharsets.ISO_8859_1));
        ServletOutputStream out = response.getOutputStream();

        ZipArchiveOutputStream zous = new ZipArchiveOutputStream(out);
        zous.setUseZip64(Zip64Mode.AsNeeded);

        String path1 = "http://www.baidu.com/img/bd_logo1.png";
        String path2 = "https://i0.hdslb.com/bfs/article/3a32f7d62c81b1e58f6f6b4e637b2602f3ec8b2c.jpg@1320w_900h.webp";
        String path3 = "https://i0.hdslb.com/bfs/article/98869629337c3d69502096162fdb8034e50ecc76.jpg@1160w_652h.webp";
        String path4 = "https://i0.hdslb.com/bfs/article/ba4df688757913f7c6a71b82af2f5c014fdd2349.png@1320w_742h.webp";
        List<String> urlList = new ArrayList<>();
        urlList.add(path1);
        urlList.add(path2);
        urlList.add(path3);
        urlList.add(path4);

        for (String path : urlList) {
            String fileName = UUID.randomUUID() + ".png";
            InputStream inputStream = ZipUtils.getIsFromUrl(path);
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
     * 批量下载文件时，需要将多个文件打包为zip，然后再下载。实现思路有两种：
     * 一是将所有文件先打包压缩为一个文件，然后下载这个压缩包。
     * 二是一边压缩一边下载，将多个文件逐一写入到压缩文件中。
     *
     * @param request  /
     * @param response /
     */
    @GetMapping(value = "/d3")
    public void download3(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");

        //设置压缩包的名字
        String downloadName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip";
        //解决不同浏览器压缩包名字含有中文时乱码的问题
        String agent = request.getHeader("USER-AGENT");
        try {
            if (agent.contains("MSIE") || agent.contains("Trident")) {
                downloadName = java.net.URLEncoder.encode(downloadName, "UTF-8");
            } else {
                downloadName = new String(downloadName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
        } catch (Exception e) {
            log.error("FileController download3 method URLEncoder error:{}", e.getMessage());
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
        ClassPathResource resource3 = new ClassPathResource("images/20210413160128024.zip");

        File f1 = resource1.getFile();
        File f2 = resource2.getFile();
        File f3 = resource3.getFile();
        File f4 = new File("d:/餐饮企业名单-经纬度.xlsx");
        List<File> fileList = new ArrayList<>();
        fileList.add(f1);
        fileList.add(f2);
        fileList.add(f3);
        fileList.add(f4);

        //循环将文件写入压缩流
        DataOutputStream os = null;
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            try {
                //添加ZipEntry，并ZipEntry中写入文件流
                //这里，加上i是防止要下载的文件有重名的导致下载失败
                Objects.requireNonNull(zipos).putNextEntry(new ZipEntry(i + file.getName()));
                os = new DataOutputStream(zipos);
                InputStream newFileIs = new FileInputStream(file);
                byte[] b = new byte[100];
                int length = 0;
                while ((length = newFileIs.read(b)) != -1) {
                    os.write(b, 0, length);
                }
                newFileIs.close();
                zipos.closeEntry();
            } catch (IOException e) {
                log.error("compressed file error:{}", e.getMessage());
            }
        }

        //关闭流
        if (os != null) {
            os.flush();
        }
        if (os != null) {
            os.close();
        }
        if (zipos != null) {
            zipos.close();
        }

    }

    @GetMapping(value = "/d4")
    public void download4(HttpServletResponse response) throws IOException {
//        ClassPathResource resource = new ClassPathResource("images/20210413160128024.zip");
        File resource = new File("d:/20210413160128024.zip");
        ZipUtils.downloadFile(resource, response);
    }
}
