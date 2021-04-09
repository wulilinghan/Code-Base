package top.b0x0.demo.http.controller.provide;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.demo.http.common.JuheResponse;
import top.b0x0.demo.http.common.R;

import java.util.HashMap;
import java.util.Map;

/**
 * post
 *
 * @author TANG
 * @since 2021-04-08
 */
@RestController
@RequestMapping("g")
@Api(tags = "Get方法")
public class GetController {

    @GetMapping("g1")
    public R g1() {
        long start = System.currentTimeMillis();
        int sum = 0;
        for (int i = 1; i < 10000; i++) {
            sum += i;
        }
        long end = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("sum", sum);
        map.put("耗时", (end - start) / 1000 + "s");
        return R.ok(map);
    }

    @GetMapping("g2")
    public R g2() {
        long start = System.currentTimeMillis();
        int sum = 0;
        for (int i = 1; i < 1000000; i++) {
            sum += i;
        }
        long end = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        map.put("sum", sum);
        map.put("耗时", (end - start) / 1000 + "s");
        return R.ok(map);
    }

}
