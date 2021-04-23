package top.b0x0.demo.redis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.b0x0.demo.redis.service.IRedisService;

import java.util.concurrent.TimeUnit;

/**
 * @author TANG
 * @since 2021-04-23
 */
@Service
public class RedisServiceImpl implements IRedisService {

    @Autowired(required = false)
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public Boolean tryLock(String lockKey, String value, long expireTime, TimeUnit timeUnit) {
        //  setnx + expire
        return redisTemplate.opsForValue().setIfAbsent(lockKey, value, expireTime, timeUnit);
    }
}
