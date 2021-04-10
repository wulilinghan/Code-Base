import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import top.b0x0.demo.http.HttpDemoApplication;
import top.b0x0.demo.http.common.R;
import top.b0x0.demo.http.common.User;

import java.util.UUID;

/**
 * WebClient测试类
 *
 * @author TANG
 * @since 2021/04/10
 */
@Slf4j
@SpringBootTest(classes = HttpDemoApplication.class)
public class WebClientTest {

    private static final WebClient client = WebClient.create("http://124.71.56.21:8081");
    // ************************** get *************************************

    @Test
    public void genUid() {
        UUID uid = UUID.randomUUID();
        System.out.println("uid = " + uid);
    }

    @Test
    public void testGetInfo() {
        Mono<R> resp = client.get().uri("/auth/getInfo")
                .retrieve()
                .bodyToMono(R.class);
        System.out.println("resp = " + resp.block());
    }

    @Test
    public void testLogout() {
        Mono<R> resp = client.get().uri("/auth/logout")
                .retrieve()
                .bodyToMono(R.class);
        System.out.println("resp = " + resp.block());
    }

    @Test
    public void testLogin() {
        Mono<R> resp = client.post().uri("/auth/login")
                .retrieve()
                .bodyToMono(R.class);
        System.out.println("resp = " + resp.block());
        //resp = R(message=, code=200, data={JSESSIONID=d2814c1d-3040-4eeb-bb8b-a1d4a03ec5f7, token=d2814c1d30404eebbb8ba1d4a03ec5f7}, timestamp=1618073950002)
    }

    private static final String JSESSIONID = "d2814c1d-3040-4eeb-bb8b-a1d4a03ec5f7";
    private static final String TOKEN = "d2814c1d30404eebbb8ba1d4a03ec5f7";

    /**
     * 使用placeholder传递参数 ,并设置请求头和cookie
     */
    @Test
    public void testGetUserList() {
        Mono<R> resp = client.get().uri("/get/user/list")
                .header("token", TOKEN)
                .cookie("JSESSIONID", JSESSIONID)
                .retrieve()
                .bodyToMono(R.class);

        System.out.println("resp = " + resp.block());
    }

    /**
     * 使用placeholder传递参数 ,并设置请求头和cookie
     */
    @Test
    public void testGetUserBYId() {
        Mono<R> resp = client.get()
                // 多个参数也可以直接放到map中,参数名与placeholder对应上即可
                .uri("/get/user/id?uid={uid}", 4) // 使用占位符
                .header("token", TOKEN)
                .cookie("JSESSIONID", JSESSIONID)
                .retrieve()
                .bodyToMono(R.class);
        System.out.println("resp = " + resp.block());
    }

    /**
     * 使用uriBuilder传递参数
     */
    @Test
    public void testGet2() {
        Mono<String> resp = WebClient.create()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("weathernew.pae.baidu.com")
                        .path("/weathernew/pc")
                        .queryParam("query", "深圳天气")
                        .queryParam("srcid", 4982)
                        .queryParam("city_name", "香港")
                        .queryParam("province_name", "香港")
                        .build())
                .retrieve()
                .bodyToMono(String.class);
        log.info("result:{}", resp.block());
    }

    // ************************** post *************************************

    /**
     * 表单提交
     */
    @Test
    public void testPostUserByForm() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("uid", "4");
        formData.add("username", "相柳");
        formData.add("age", "14");
        formData.add("address", "梧桐山");

        Mono<R> resp = client.post()
                .uri("post/user/addByForm")
                .header("token", TOKEN)
                .cookie("JSESSIONID", JSESSIONID)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve().bodyToMono(R.class);

        log.info("result:{}", resp.block());
    }

    /**
     * JSON提交数据  通过bean方式
     */
    @Test
    public void testPostUserByJson1() {
        User user = new User().setUid(5).setUsername("八岐大蛇").setAge(119).setAddress("东海");

        Mono<R> resp = client.post()
                .uri("post/user/addByJson")
                .header("token", TOKEN)
                .cookie("JSESSIONID", JSESSIONID)
                .contentType(MediaType.APPLICATION_JSON)
                // 使用bean方式提交JSON数据
                .body(Mono.just(user), User.class)
                .retrieve().bodyToMono(R.class);

        log.info("result:{}", resp.block());
    }

}
