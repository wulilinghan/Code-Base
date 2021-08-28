package top.b0x0.demo.distributedLock.domain;

/**
 * @author TANG
 * @since 2021-04-23
 */
public class RedisKey {
    /**
     * orderLock:[machineId]
     */
    public static final String ORDER_LOCK_KEY = "orderLock:%s";
}
