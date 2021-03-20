package top.b0x0.demo.mybatis;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.b0x0.demo.mybatis.util.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TANG
 * @date 2021-02-14
 */
@SpringBootTest
public class RandomUtilsTest {

    private static List<String> nameList = new ArrayList<>();
    private static List<String> sexList = new ArrayList<>();

    static {

        nameList.add("张三");
        nameList.add("李四");
        nameList.add("王五");
        nameList.add("小六");
        nameList.add("唐僧");
        nameList.add("猪八戒");
        nameList.add("沙僧");
        nameList.add("白龙马");
        nameList.add("孙悟空");
        nameList.add("六耳猕猴");

        sexList.add("1");
        sexList.add("0");
    }

    @Test
    public void test1() {
        List<String> randomChooseElements = RandomUtils.randomChooseElements(sexList, 1);
        System.out.println("randomChooseElements.toString() = " + randomChooseElements.toString());
        String sex = randomChooseElements.get(0);
        List<String> randomChooseElements2 = RandomUtils.randomChooseElements(nameList, 1);
        String name = randomChooseElements2.get(0);
        System.out.println("randomChooseElements2.toString() = " + randomChooseElements2.toString());
    }
}
