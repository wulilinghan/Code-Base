package top.b0x0.demo.distributedLock.db.service;

import top.b0x0.demo.distributedLock.domain.DistributedLock;

/**
 * @author TANG
 * @since 2021/04/27
 */
public interface DistributedLockService {

    /**
     * 获取锁
     *
     * @param distributedLock /
     * @return /
     */
    boolean tryLock(DistributedLock distributedLock);

    /**
     * 释放锁
     *
     * @param distributedLock /
     * @return /
     */
    boolean unlock(DistributedLock distributedLock);
}
