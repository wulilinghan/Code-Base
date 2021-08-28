package top.b0x0.demo.distributedLock.db.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.b0x0.demo.distributedLock.domain.DistributedLock;

/**
 * @author TANG
 * @since 2021/04/26
 */
public interface DistributedLockMapper extends BaseMapper<DistributedLock> {

    /**
     * 获取锁
     *
     * @param lock /
     * @return /
     */
    DistributedLock getLockByResource(DistributedLock lock);

    /**
     * 上锁
     *
     * @param lock /
     * @return /
     */
    int locked(DistributedLock lock);

    /**
     * 释放锁
     *
     * @param lock /
     * @return /
     */
    int releaseLock(DistributedLock lock);

    /**
     * 新增锁数据
     *
     * @param lock /
     * @return /
     */
    int insertLock(DistributedLock lock);

    /**
     * 释放锁
     *
     * @param resource /
     * @return /
     */
    int delByResource(String resource);
}
