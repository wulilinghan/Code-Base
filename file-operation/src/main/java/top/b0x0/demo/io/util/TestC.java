package top.b0x0.demo.io.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static top.b0x0.demo.io.util.CompressUtils.doZip;
import static top.b0x0.demo.io.util.CompressUtils.doZip4Url;

/**
 * 1. 将多个远端文件读取并压缩  doZip4Url()
 * 2. 将本地某个文件/文件夹压缩 doZip()
 *
 * @author musui
 */
@Slf4j
public class TestC {

    private static final String PATH_1 = "http://www.baidu.com/img/bd_logo1.png";
    private static final String PATH_2 = "http://www.baidu.com/img/bd_logo1.png";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final String OUT_ZIP_FILE = "d:/test/" + SIMPLE_DATE_FORMAT.format(new Date()) + ".zip";
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

    public static void main(String[] args) throws IOException, ServletException {
        System.out.println("os.name " + System.getProperty("os.name"));
        boolean win = SYS_OS.toLowerCase().startsWith("win");
        System.out.println("win = " + win);

        Map<String, String> fileMap = new HashMap<>(2);
        fileMap.put("logo1.png", PATH_1);
        fileMap.put("logo2.png", PATH_2);

        doZip4Url(fileMap, OUT_ZIP_FILE);

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
        doZip(fileList, targetFile);

    }





}