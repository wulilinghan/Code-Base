package top.b0x0.demo.http.controller.provide;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.demo.http.common.R;
import top.b0x0.demo.http.common.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * post
 *
 * @author TANG
 * @since 2021-04-08
 */
@RestController
@RequestMapping("get")
@Api(tags = "Get方法")
@Slf4j
public class GetController{

    @Autowired
    CommonData commonData;

    @GetMapping("user/list")
    public R userList() {
        log.info("request method: {}", "get/user/list");
        return R.ok(commonData.getUserList());
    }

    @GetMapping("user/id")
    public R userById(@RequestParam("uid") String uid) {
        List<User> userList = commonData.getUserList();
        Optional<User> first = userList.stream().filter(e -> e.getUid() == Integer.parseInt(uid)).findAny();
        User user = first.orElse(null);
        log.info("request method: {}, param:{}", "get/user/id", uid);
        return R.ok(user);

    }

    @GetMapping("sum")
    public R sum(String endNum) {
        long num = Long.parseLong(endNum);
        long start = System.currentTimeMillis();
        long sum = 0;
        for (int i = 1; i < num; i++) {
            sum += i;
        }
        long end = System.currentTimeMillis();
        System.out.println("1到" + endNum + "求和 = " + sum);
        Map<String, Object> map = new HashMap<>(2);
        map.put("sum", sum);
        map.put("耗时", (end - start) / 1000 + "s");
        return R.ok(map);
    }

}
