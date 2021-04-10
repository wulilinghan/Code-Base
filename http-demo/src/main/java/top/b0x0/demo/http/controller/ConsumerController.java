package top.b0x0.demo.http.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import top.b0x0.demo.http.common.JuheResponse;
import top.b0x0.demo.http.common.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static top.b0x0.demo.http.common.CommonConstants.*;

/**
 * 测试controller
 *
 * @author TANG
 * @since 2021-04-08
 */
@RestController
@RequestMapping("test")
@Api(tags = "消费者")
public class ConsumerController {
    private static final Logger log = LoggerFactory.getLogger(ConsumerController.class);

    private static final String RE_HTTP = "http://124.71.56.21:8081";

    @Autowired
    RestTemplate restTemplate;

    static DateTimeFormatter YMDHMS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static DateTimeFormatter MD_FORMATTER = DateTimeFormatter.ofPattern("M/d");

    /****************************** restTemplate **********************************/

    @GetMapping("t3")
    public Map<String, Object> test3() {
        String url1 = RE_HTTP + "/g/g1";
        ResponseEntity<JuheResponse> reps1 = restTemplate.getForEntity(url1, JuheResponse.class);
        System.out.println("reps1 = " + reps1);

        String url2 = RE_HTTP + "/g/g2";
        ResponseEntity<JuheResponse> resp2 = restTemplate.postForEntity(url2, null, JuheResponse.class);
        System.out.println("resp2 = " + resp2);
        Map<String, Object> map = new HashMap<>(2);
        map.put("juheResponse1", reps1);
        map.put("juheResponse2", resp2);
        return map;
    }

    @GetMapping("t2")
    public Map<String, Object> test2() {
        String url1 = RE_HTTP + "/p/p1";
        ResponseEntity<JuheResponse> reps1 = restTemplate.postForEntity(url1, null, JuheResponse.class);
        System.out.println("reps1.getStatusCode() = " + reps1.getStatusCode());
        System.out.println("reps1.getStatusCode().is2xxSuccessful() = " + reps1.getStatusCode().is2xxSuccessful());
        System.out.println("reps1 = " + reps1);

        String url2 = RE_HTTP + "/p/p2";
        ResponseEntity<JuheResponse> resp2 = restTemplate.postForEntity(url2, null, JuheResponse.class);
        System.out.println("resp2 = " + resp2);
        Map<String, Object> map = new HashMap<>(2);
        map.put("juheResponse1", reps1);
        map.put("juheResponse2", resp2);
        return map;
    }

    @GetMapping("t1")
    public Map<String, Object> test1() {
        JuheResponse juheResponse = todayOfHistory();
        System.out.println("juheResponse = " + juheResponse);

        JuheResponse juheResponse1 = getPhoneAttribution();
        System.out.println("juheResponse1 = " + juheResponse1);
        Map<String, Object> map = new HashMap<>(2);
        map.put("juheResponse1", juheResponse);
        map.put("juheResponse2", juheResponse1);
        return map;
    }

    private JuheResponse getPhoneAttribution() {
        String reUrl = URL_MOBILE_PHONE_NUMBER_ATTRIBUTION +
                "?" + "key=" + KEY_MOBILE_PHONE_NUMBER_ATTRIBUTION +
                "&" + "phone=" + "18373342114" +
                "&" + "dtype" + "";
        log.info(reUrl);

        String object = restTemplate.getForObject(reUrl, String.class);
        return JSON.parseObject(object, JuheResponse.class);
    }

    private JuheResponse todayOfHistory() {
        String todayDate = MD_FORMATTER.format(LocalDate.now());

        String reUrl = URL_TODAY_IN_HISTORY +
                "?" + "key=" + KEY_TODAY_IN_HISTORY +
                "&" + "date=" + todayDate;

        log.info(reUrl);
        String object = restTemplate.getForObject(reUrl, String.class);
        return JSON.parseObject(object, JuheResponse.class);
    }

    /** **************************** WebClient **********************************/

    /**
     * Spring5带来了新的响应式web开发框架WebFlux，同时，也引入了新的HttpClient框架WebClient。WebClient是Spring5中引入的执行 HTTP 请求的非阻塞、反应式客户端。
     * 它对同步和异步以及流方案都有很好的支持，WebClient发布后，RestTemplate将在将来版本中弃用，并且不会向前添加主要新功能。
     */
    @GetMapping("w1")
    public R w1() {
        WebClient webClient = WebClient.create(RE_HTTP);
        Mono<R> bodyToMono = webClient.get().uri("/get/sum?endNum={num}", 1000000000).retrieve().bodyToMono(R.class);
        return bodyToMono.block();
    }

    @GetMapping("w3")
    public R w3() {
        Mono<String> resp = WebClient.create()
                .get()
                //多个参数也可以直接放到map中,参数名与placeholder对应上即可
                //使用占位符
                .uri("http://www.baidu.com/s?wd={key}&other={another}", "深圳天气", "test")
                .retrieve()
                .bodyToMono(String.class);
        log.info("result:{}", resp.block());
        return R.ok(resp);
    }

    @GetMapping("w2")
    public R w2() {
        WebClient webClient = WebClient.create(RE_HTTP);
        Mono<R> bodyToMono = webClient.get().uri("/g/g2").retrieve().bodyToMono(R.class);
        R block = bodyToMono.block();
        return block;
    }
}
