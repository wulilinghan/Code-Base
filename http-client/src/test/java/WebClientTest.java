import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import top.b0x0.demo.http.HttpDemoApplication;
import top.b0x0.demo.http.common.R;
import top.b0x0.demo.http.common.User;

import java.io.Serializable;
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

    private static final String JSESSIONID = "6dfd8a2d-d6de-40e5-9611-3f604b402a8b";
    private static final String TOKEN = "6dfd8a2dd6de40e596113f604b402a8b";
    private static final String BASE_URL = "http://124.71.56.21:8081";

    private static final WebClient client = WebClient.builder()
            .baseUrl(BASE_URL)
            .defaultHeader("token", TOKEN)
            .defaultCookie("JSESSIONID", JSESSIONID)
            .build();

    /************************************** GET *************************************/

    @Test
    public void genUid() {
        UUID uid = UUID.randomUUID();
        System.out.println("uid = " + uid);
    }

    /**
     * 使用placeholder传递参数 ,并设置请求头和cookie
     */
    @Test
    public void testGetUserList() {
        Mono<R> resp = client.get().uri("/get/user/list")
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

    /************************************** POST *************************************/

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
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve().bodyToMono(R.class);

        log.info("result:{}", resp.block());
    }

    /**
     * JSON提交数据
     */
    @Test
    public void testPostUserByJson() {
        User user = new User().setUid(5).setUsername("八岐大蛇").setAge(119).setAddress("东海");

        //  1. 通过bean方式
        Mono<R> resp1 = client.post()
                .uri("post/user/addByJson")
                .contentType(MediaType.APPLICATION_JSON)
                // 使用bean方式提交JSON数据
                .body(Mono.just(user), User.class)
                .retrieve().bodyToMono(R.class);

        // 2. 通过JSON字符串
        Mono<R> resp2 = client.post()
                .uri("post/user/addByJson")
                .contentType(MediaType.APPLICATION_JSON)
                // 使用bean方式提交JSON数据
                .body(BodyInserters.fromValue(JSON.toJSONString(user)))
                .retrieve().bodyToMono(R.class);

        log.info("\nresult1:{}", resp1.block());
        log.info("\nresult2:{}", resp2.block());
    }

    /**
     * 上传文件  设置异常处理
     * <p>
     * onStatus根据status code进行异常适配
     * doOnError异常适配
     * onErrorReturn返回默认值
     */
    @Test
    public void testUploadFile() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

//        HttpEntity<ClassPathResource> entity1 = new HttpEntity<>(new ClassPathResource("yuque_diagram.jpg"), headers);
        HttpEntity<ClassPathResource> entity1 = new HttpEntity<>(new ClassPathResource("yuque_diagram.jpg"));
//        HttpEntity<ClassPathResource> entity2 = new HttpEntity<>(new ClassPathResource("线程类图.png"), headers);
        HttpEntity<ClassPathResource> entity2 = new HttpEntity<>(new ClassPathResource("线程类图.png"));
        HttpEntity<ClassPathResource> entity3 = new HttpEntity<>(new ClassPathResource("acane_madder - 庭園にて。.mp3"));

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
//        parts.add("file", entity1);
        parts.add("file", entity2);

        WebClient.ResponseSpec responseSpec = client.post()
                .uri("/file/up1")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(parts))
                .retrieve();

        Mono<R> onReturn = responseSpec.onStatus(
                HttpStatus::is4xxClientError, resp -> {
                    log.error("error:{}, msg:{}", resp.statusCode().value(), resp.statusCode().getReasonPhrase());
                    return Mono.error(new RuntimeException(resp.statusCode().value() + " : " + resp.statusCode().getReasonPhrase()));
                }
        )
                .bodyToMono(R.class)
                .doOnError(
                        WebClientResponseException.class, err -> {
                            log.info("ERROR status:{}, msg:{}", err.getRawStatusCode(), err.getResponseBodyAsString());
                            throw new RuntimeException(err.getMessage());
                        }
                )
                .onErrorReturn(R.fail("网络异常"));

        log.info("\nresult: {}", onReturn.block());
    }

    @Test
    public void testUploadFile2() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

//        HttpEntity<ClassPathResource> entity1 = new HttpEntity<>(new ClassPathResource("yuque_diagram.jpg"), headers);
        HttpEntity<ClassPathResource> entity1 = new HttpEntity<>(new ClassPathResource("yuque_diagram.jpg"));
//        HttpEntity<ClassPathResource> entity2 = new HttpEntity<>(new ClassPathResource("线程类图.png"), headers);
        HttpEntity<ClassPathResource> entity2 = new HttpEntity<>(new ClassPathResource("线程类图.png"));
        HttpEntity<ClassPathResource> entity3 = new HttpEntity<>(new ClassPathResource("acane_madder - 庭園にて。.mp3"));

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", entity1);
        parts.add("file", entity2);
        parts.add("file", entity3);

        Mono<Serializable> serializableMono = client.post()
                .uri("/file/up1?isErr={isErr}", "isErr")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(parts))
                .exchangeToMono(
                        resp -> {
                            if (resp.statusCode().equals(HttpStatus.OK)) {
                                return resp.bodyToMono(R.class);
                            } else if (resp.statusCode().is4xxClientError()) {
                                return Mono.error(new RuntimeException(resp.statusCode().value() + " : " + resp.statusCode().getReasonPhrase()));
                            } else {
                                return resp.createException().flatMap(Mono::error);
                            }
                        }
                );
        R block = ((R) serializableMono.block());
        System.out.println("block = " + block);
    }

}
