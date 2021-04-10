package top.b0x0.demo.http.controller.provide;

import org.springframework.stereotype.Component;
import top.b0x0.demo.http.common.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TANG
 * @since 2021/04/10
 */
@Component
public class CommonData {

    private static final List<User> USER_LIST = new ArrayList<>();

    static {
        USER_LIST.add(new User(1, "张三", 14, "长沙市"));
        USER_LIST.add(new User(2, "李四", 11, "武汉市"));
        USER_LIST.add(new User(3, "王五", 19, "北京市"));
    }

    public List<User> getUserList() {
        return USER_LIST;
    }
}
