package top.b0x0.demo.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedisDemoApplicationTests {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Test
    void contextLoads() {
    }

    /**
     * tryLock
     *
     * @param lockKey    /
     * @param value      /
     * @param expireTime /
     * @param timeUnit   /
     * @return boolean
     */
    public boolean tryLock(String lockKey, String value, long expireTime, TimeUnit timeUnit) {
        // 1.先setnx
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, value);
        if (lock != null && lock) {
            // 2.再expire
            redisTemplate.expire(lockKey, expireTime, timeUnit);
            return true;
        } else {
            return false;
        }
    }
}
