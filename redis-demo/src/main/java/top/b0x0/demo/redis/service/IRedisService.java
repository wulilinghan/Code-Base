package top.b0x0.demo.redis.service;

import java.util.concurrent.TimeUnit;

/**
 * @author TANG
 * @since 2021-04-23
 */
public interface IRedisService {

    /**
     * 上锁
     *
     * @param lockKey    锁
     * @param value      对应的值
     * @param expireTime 过期时间
     * @param timeUnit   时间单位
     * @return boolean
     */
    boolean tryLock(String lockKey, String value, long expireTime, TimeUnit timeUnit);

    /**
     * 释放锁
     *
     * @param lockKey 锁
     * @param value   对应的值
     * @return boolean
     */
    boolean unLock(String lockKey, String value);
}
