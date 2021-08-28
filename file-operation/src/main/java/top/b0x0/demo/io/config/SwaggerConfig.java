package top.b0x0.demo.io.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author TANG
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("top.b0x0.demo"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * name:开发者姓名
     * url:开发者网址
     * email:开发者邮箱
     *
     * @return /
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 标题
                .title("io-demo接口文档")
                // 文档接口的描述
                .description("io-demo")
                .contact(new Contact("TANG", "tanglinghan.github.io", "1902325071@qq.com"))
                .version("1.0.0")
                .build();
    }

}