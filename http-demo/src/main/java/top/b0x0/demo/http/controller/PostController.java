package top.b0x0.demo.http.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.demo.http.common.JuheResponse;

/**
 * post
 *
 * @author TANG
 * @since 2021-04-08
 */
@RestController
@RequestMapping("p")
public class PostController {

    @PostMapping("p1")
    public JuheResponse p1() throws InterruptedException {
        Thread.sleep(1000 * 10);
        return new JuheResponse().setResult("P1执行完成").setResultcode("200");
    }


    @PostMapping("p2")
    public JuheResponse p2() throws InterruptedException {
        Thread.sleep(1000 * 5);
        return new JuheResponse().setResult("P2执行完成").setResultcode("200");
    }

}
