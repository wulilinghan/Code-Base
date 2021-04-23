package top.b0x0.demo.redis.service;

import java.util.concurrent.TimeUnit;

/**
 * @author TANG
 * @since 2021-04-23
 */
public interface IRedisService {

    /**
     * tryLock
     *
     * @param lockKey    锁
     * @param value      对应的值
     * @param expireTime 过期时间
     * @param timeUnit   时间单位
     * @return boolean
     */
    Boolean tryLock(String lockKey, String value, long expireTime, TimeUnit timeUnit);
}
