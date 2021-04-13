package top.b0x0.demo.io;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author TANG
 * @since 2021/04/13
 */
public class TestA {
    private static final String PATH_1 = "http://www.baidu.com/img/bd_logo1.png";
    private static final String PATH_2 = "http://www.baidu.com/img/bd_logo1.png";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final String ZIPFILE_OUT_PATH = "d:/test/" + SIMPLE_DATE_FORMAT.format(new Date()) + ".zip";
    private static final String SYS_OS;
    private static boolean SYS_OS_IS_WIM = false;
    private static boolean SYS_OS_IS_LINUX = false;

    static {
        SYS_OS = System.getProperty("os.name");
        System.out.println("SYS_OS = " + SYS_OS);
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
/*
        Map<String, String> fileMap = new HashMap<>(2);
        fileMap.put("logo1.png", PATH_1);
        fileMap.put("logo2.png", PATH_2);

        zipUrlFile(fileMap, new ZipOutputStream(new FileOutputStream(new File(ZIPFILE_OUT_PATH))));
*/

/*
        String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("images/20210413160128024.zip")).getPath();
        System.out.println("path = " + path);
*/
/*
        File srcFile1 = new File("d:/test");
        File srcFile2 = new File("d:/Java开发手册（嵩山版）.pdf");
        File srcFile4 = new File("d:");
        File srcFile3 = new File("D:\\test\\temp\\gulimall-learning-master_20210227_111613012.zip");

        File targetFile = new File("d:/test/zip-test/out-zip/out" + new SimpleDateFormat("yyyyMMddHHmmssSSS") + ".zip");

        List<File> fileList = new ArrayList<>();
        fileList.add(srcFile1);
        fileList.add(srcFile2);
        fileList.add(srcFile4);
        zipFiles(fileList, targetFile);
*/

        // -----------------------------------------------------------------------------------------------------
        File srcFile1 = new File("/home");
        File srcFile2 = new File("/home/Java开发手册（嵩山版）.pdf");
//        File srcFile4 = new File("/");
        File srcFile3 = new File("/home/testtemp/gulimall-learning-master_20210227_111613012.zip");

        File targetZipFile = new File("/home/test/zip-test/out-zip/out" + new SimpleDateFormat("yyyyMMddHHmmssSSS") + ".zip");
//
        List<File> srcFileList = new ArrayList<>();
        srcFileList.add(srcFile1);
        srcFileList.add(srcFile2);
//        srcFileList.add(srcFile4);

        System.out.println("File.separator = " + File.separator);
        String[] targetFileSplit = targetZipFile.getParentFile().getPath().split("/");
        System.out.println("srcFileList.size() = " + srcFileList.size());
        for (File file : srcFileList) {
            String filePath = file.getPath();
            System.out.println("filePath = " + filePath);
            String[] split = filePath.split("/");
            System.out.println("split = " + Arrays.toString(split));
            System.out.println("split.length = " + split.length);
/*            if (split.length >= 2) {
                if (split[0].equals(targetFileSplit[0])) {
//                    throw new IOException("源文件目录与目标文件路径冲突");
                    System.err.println("源文件目录与目标文件路径冲突");
                }
            } else {
//                throw new IOException("源文件是系统根目录");
                System.err.println("源文件是系统根目录");
            }*/
        }
    }
}
