package top.b0x0.demo.redis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
    public boolean tryLock(String lockKey, String value, long expireTime, TimeUnit timeUnit) {
        try {
            //  setnx + expire
            Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent(lockKey, value, expireTime, timeUnit);
            if (Boolean.TRUE.equals(setIfAbsent)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean unLock(String lockKey, String value) {
        String val = (String) redisTemplate.opsForValue().get(lockKey);
        if (val != null && !StringUtils.hasText(val) && val.equals(value)) {
            redisTemplate.delete(lockKey);
            return true;
        }
        return false;
    }
}
