package top.b0x0.demo.http.controller.provide;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.demo.http.common.R;
import top.b0x0.demo.http.common.UserAuthUtils;

import java.util.Map;

/**
 * @author TANG
 * @since 2021/04/10
 */
@RestController
@RequestMapping("auth")
@Api(tags = "登录")
public class LoginController {

    @PostMapping("login")
    public R login() {
        Map<String, String> genToken = UserAuthUtils.genToken();
        return R.ok(genToken);
    }

    @PostMapping("logout")
    public R logout() {
        UserAuthUtils.logout();
        return R.ok();
    }

    @GetMapping("getInfo")
    public R getInfo() {
        return R.ok(UserAuthUtils.USER_AUTH.get());
    }

    @GetMapping("401")
    public R notAuth() {
        return R.notAuth();
    }
}
