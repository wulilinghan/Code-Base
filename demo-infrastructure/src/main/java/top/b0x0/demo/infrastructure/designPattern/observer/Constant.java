package top.b0x0.demo.infrastructure.designPattern.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TANG
 * @since 2021-03-30
 */
public class Constant {
    public static final String USER_ZHANGSAN = "张三";
    public static final String USER_LISI = "李四";
    public static String[] initAnime = {"灵笼", "斗罗大陆"};
    public static List<String> animeList = new ArrayList<>();

    static {
        animeList.add("灵笼");
        animeList.add("斗罗大陆");
    }
}
