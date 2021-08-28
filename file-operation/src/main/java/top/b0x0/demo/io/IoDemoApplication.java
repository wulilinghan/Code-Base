package top.b0x0.demo.io;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author musui
 */
@SpringBootApplication
@MapperScan("top.b0x0.demo.io.dao")
public class IoDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(IoDemoApplication.class, args);
    }

}
