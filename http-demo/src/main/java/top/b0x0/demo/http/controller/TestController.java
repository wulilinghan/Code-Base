package top.b0x0.demo.http.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import top.b0x0.demo.http.common.JuheResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static top.b0x0.demo.http.common.CommonConstants.*;

/**
 * 测试controller
 *
 * @author TANG
 * @since 2021-04-08
 */
@RestController
@RequestMapping("test")
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    RestTemplate restTemplate;

    static DateTimeFormatter YMDHMS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static DateTimeFormatter MD_FORMATTER = DateTimeFormatter.ofPattern("M/d");

    @GetMapping("t2")
    public void test2() {
        String url1 = "http://124.71.56.21:8081/p/p1";
        ResponseEntity<JuheResponse> reps1 = restTemplate.postForEntity(url1, null, JuheResponse.class);
        System.out.println("reps1 = " + reps1);

        String url2 = "http://124.71.56.21:8081/p/p2";
        ResponseEntity<JuheResponse> resp2 = restTemplate.postForEntity(url2, null, JuheResponse.class);
        System.out.println("resp2 = " + resp2);
    }

    @GetMapping("t1")
    public void test1() {
        JuheResponse juheResponse = todayOfHistory();
        System.out.println("juheResponse = " + juheResponse);

        JuheResponse juheResponse1 = getPhoneAttribution();
        System.out.println("juheResponse1 = " + juheResponse1);
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

}
