package top.b0x0.demo.distributedLock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 分布式锁实现
 *
 * @author TANG
 */
@SpringBootApplication
@MapperScan("top.b0x0.demo.distributedLock.db.dao")
public class DistributedLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedLockApplication.class, args);
    }

}
