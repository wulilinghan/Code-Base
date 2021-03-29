package top.b0x0.demo.infrastructure;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Thumbnailator包，基于Java自带的图片IO流，处理效率很高；但是如果需要处理HEIC等格式，还是需要ImageMagick这样的系统工具包支持：先系统安装ImageMagick，然后Java调用API。
 * 通常情况下，Thumbnailator和webp-imageio-core配合，已经可以满足绝大多数Java创建的工程需求。
 *
 * @author musui
 * @since 2021-03-25
 */
//@RunWith(SpringRunner.class)
@SpringBootTest()
public class ThumbnailatorTests {

    private static final String path = "D:\\work-space\\ower\\demo-collection\\io-demo\\src\\main\\resources\\images\\test.jpg";
    private static final String out_path = "D:\\work-space\\ower\\demo-collection\\io-demo\\src\\main\\resources\\images_out\\";

    @Test
    void contextLoads() {
    }

    /**
     * 指定大小进行缩放
     */
    @Test
    public void test1() throws IOException {
        /*
         * size(width,height) 若图片横比200小，高比300小，不变
         * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
         * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
         */
        Thumbnails.of(path).size(200, 300).toFile(out_path + "image_200x300.jpg");
        Thumbnails.of(path).size(2560, 2048).toFile(out_path + "image_2560x2048.jpg");
    }

    /**
     * 按照比例进行缩放
     */
    @Test
    public void test2() throws IOException {
        /*
         * scale(比例)
         */
        Thumbnails.of(path).scale(0.25f).toFile(out_path + "image_25%.jpg");
        Thumbnails.of(path).scale(1.50f).toFile(out_path + "image_150%.jpg");
    }

    /**
     * 不按照比例，指定大小进行缩放
     */
    @Test
    public void test3() throws IOException {
        /*
         * keepAspectRatio(false) 默认是按照比例缩放的
         */
        Thumbnails.of(path).size(120, 120).keepAspectRatio(false).toFile(out_path + "image_120x120.jpg");
    }

    /**
     * 旋转
     */
    @Test
    public void test4() throws IOException {
        /*
         * rotate(角度),正数：顺时针 负数：逆时针
         */
        Thumbnails.of(path).size(1280, 1024).rotate(90).toFile(out_path + "image+90.jpg");
        Thumbnails.of(path).size(1280, 1024).rotate(-90).toFile(out_path + "iamge-90.jpg");
    }

    /**
     * 水印
     */
    @Test
    public void test5() throws IOException {
        /*
         * watermark(位置，水印图，透明度)
         */
        Thumbnails.of(path).size(1280, 1024).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File("images/watermark.png")), 0.5f)
                .outputQuality(0.8f).toFile(out_path + "image_watermark_bottom_right.jpg");
        Thumbnails.of(path).size(1280, 1024).watermark(Positions.CENTER, ImageIO.read(new File("images/watermark.png")), 0.5f)
                .outputQuality(0.8f).toFile(out_path + "image_watermark_center.jpg");
    }

    /**
     * 裁剪
     */
    @Test
    public void test6() throws IOException {
        /*
         * 图片中心400*400的区域
         */
        Thumbnails.of(path).sourceRegion(Positions.CENTER, 400, 400).size(200, 200).keepAspectRatio(false)
                .toFile(out_path + "image_region_center.jpg");
        /*
         * 图片右下400*400的区域
         */
        Thumbnails.of(path).sourceRegion(Positions.BOTTOM_RIGHT, 400, 400).size(200, 200).keepAspectRatio(false)
                .toFile(out_path + "image_region_bootom_right.jpg");
        /*
         * 指定坐标
         */
        Thumbnails.of(path).sourceRegion(600, 500, 400, 400).size(200, 200).keepAspectRatio(false).toFile(out_path + "image_region_coord.jpg");
    }

    /*
     * 转化图像格式
     */
    @Test
    public void test7() throws IOException {
        /*
         * outputFormat(图像格式)
         */
        Thumbnails.of(path).size(1280, 1024).outputFormat("png").toFile(out_path + "image_1280x1024.png");
        Thumbnails.of(path).size(1280, 1024).outputFormat("gif").toFile(out_path + "image_1280x1024.gif");
    }

    /**
     * 输出到OutputStream
     */
    @Test
    public void test8() throws IOException {
        /*
         * toOutputStream(流对象)
         */
        OutputStream os = new FileOutputStream(out_path + "image_1280x1024_OutputStream.png");
        Thumbnails.of(path).size(1280, 1024).toOutputStream(os);
    }

    /**
     * 输出到BufferedImage
     */
    @Test
    public void test9() throws IOException {
        /*
         * asBufferedImage() 返回BufferedImage
         */
        BufferedImage thumbnail = Thumbnails.of(path).size(1280, 1024).asBufferedImage();
        ImageIO.write(thumbnail, "jpg", new File(out_path + "image_1280x1024_BufferedImage.jpg"));
    }
}
