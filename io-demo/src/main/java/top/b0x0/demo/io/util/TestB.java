package top.b0x0.demo.io.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TANG
 * @since 2021/04/20
 */
public class TestB {
    static String srcFilename1 = "D:\\tmp\\工作簿1.xlsx";
    static String srcFilename2 = "D:\\tmp\\工作簿1 - 副本.xlsx";
    static String srcFilename4 = "D:\\tmp\\新建文件夹 (2)\\工作簿1.xlsx";
    static String srcFilename3 = "D:\\tmp\\新建文件夹";

    static File srcFile1 = new File(srcFilename1);
    static File srcFile2 = new File(srcFilename2);
    static File srcFile4 = new File(srcFilename4);
    static File srcFile3 = new File(srcFilename3);

    static List<File> srcFileList = new ArrayList<>();

    static {
        srcFileList.add(srcFile1);
        srcFileList.add(srcFile2);
        srcFileList.add(srcFile4);
    }

    public static void main(String[] args) throws Exception {

        srcFileList.add(srcFile3);
        CompressUtils.doTarGz(srcFileList, "d:/tmp/targz/out.tar.gz");


    }
}
