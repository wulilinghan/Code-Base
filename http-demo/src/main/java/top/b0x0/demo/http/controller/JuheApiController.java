package top.b0x0.demo.http.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("juhe")
public class JuheApiController {
    private static final Logger log = LoggerFactory.getLogger(JuheApiController.class);

    @Autowired
    RestTemplate restTemplate;

    static DateTimeFormatter YMDHMS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static DateTimeFormatter MD_FORMATTER = DateTimeFormatter.ofPattern("M/d");

    @GetMapping("t1")
    public void test1() {
        String todayDate = MD_FORMATTER.format(LocalDate.now());
        JuheResponse juheResponse = todayOfHistory(todayDate);

        System.out.println("juheResponse = " + juheResponse);

        String phone = "18373342114";
        JuheResponse juheResponse1 = getPhoneAttribution(phone);

        System.out.println("juheResponse1 = " + juheResponse1);
    }

    private JuheResponse getPhoneAttribution(String phone) {
        String reUrl = URL_MOBILE_PHONE_NUMBER_ATTRIBUTION +
                "?" + "key=" + KEY_MOBILE_PHONE_NUMBER_ATTRIBUTION +
                "&" + "phone=" + phone +
                "&" + "dtype" + "";
        log.info(reUrl);

        String object = restTemplate.getForObject(reUrl, String.class);
        return JSON.parseObject(object, JuheResponse.class);
    }

    private JuheResponse todayOfHistory(String todayDate) {
        String reUrl = URL_TODAY_IN_HISTORY +
                "?" + "key=" + KEY_TODAY_IN_HISTORY +
                "&" + "date=" + todayDate;

        log.info(reUrl);
        String object = restTemplate.getForObject(reUrl, String.class);
        return JSON.parseObject(object, JuheResponse.class);
    }

}
