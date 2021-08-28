package top.b0x0.demo.http.controller.provide;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.demo.http.common.R;
import top.b0x0.demo.http.common.User;

import java.util.List;
import java.util.Map;

/**
 * post
 *
 * @author TANG
 * @since 2021-04-08
 */
@RestController
@RequestMapping("post")
@Api(tags = "Post方法")
@Slf4j
public class PostController {

    @Autowired
    CommonData commonData;

    @PostMapping("user/addByForm")
    @ApiOperation("表单接收")
    public R addByForm(@Validated User user) {
        log.info("request method: {}, param:{}", "post/user/addByForm", user);

        List<User> userList = commonData.getUserList();
        userList.add(user);

        return R.ok(user);
    }

    @PostMapping("user/addByJson")
    @ApiOperation("JSON接收")
    public R addByJson(@Validated @RequestBody User user) {
        log.info("request method: {}, param:{}", "post/user/addByJson", user);

        List<User> userList = commonData.getUserList();
        userList.add(user);

        return R.ok(user);
    }

    @PostMapping("user/updateUser")
    @ApiOperation("JSON修改")
    public R updateUser(@Validated @RequestBody User user) {
        log.info("request method: {}, param:{}", "post/user/updateUser", user);

        if (user.getUid() == null) {
            return R.fail("参数异常");
        }

        List<User> userList = commonData.getUserList();
        boolean isExist = false;
        for (User targetUser : userList) {
            if (targetUser.getUid().equals(user.getUid())) {
                isExist = true;
                BeanUtils.copyProperties(user, targetUser);
            }
        }
        if (!isExist) {
            return R.fail("未找到匹配数据");
        }
        return R.ok();
    }

    @PostMapping("sysEnv")
    public R sysEnv() {
        Map<String, String> stringMap = System.getenv();
        return R.ok(stringMap);
    }
}
