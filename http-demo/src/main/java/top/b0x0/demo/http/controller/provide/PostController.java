package top.b0x0.demo.http.controller.provide;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.demo.http.common.JuheResponse;

import java.util.Map;

/**
 * post
 *
 * @author TANG
 * @since 2021-04-08
 */
@RestController
@RequestMapping("p")
@Api(tags = "Post方法")
public class PostController {

    @PostMapping("p1")
    public JuheResponse p1() throws InterruptedException {
        long start = System.currentTimeMillis();
        System.out.println(" p1 zhi xing le .... ");
        Thread.sleep(1000 * 10);
        long end = System.currentTimeMillis();
        return new JuheResponse().setResult("P1执行完成").setResultcode("200").setResult("zhi xing shi jian:" + (end - start) / 1000 + "s");
    }

    @PostMapping("p2")
    public JuheResponse p2() throws InterruptedException {
        long start = System.currentTimeMillis();

        Thread.sleep(1000 * 5);
        long end = System.currentTimeMillis();

        return new JuheResponse().setResult("P2执行完成").setResultcode("200").setResult("zhi xing shi jian:" + (end - start) / 1000 + "s");
    }

    @PostMapping("p3/sysEnv")
    public JuheResponse p3() {
        Map<String, String> stringMap = System.getenv();
        return JuheResponse.ok(stringMap);
    }
}
