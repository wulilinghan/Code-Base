package top.b0x0.demo.distributedLock.db;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.b0x0.demo.distributedLock.domain.DistributedLock;

/**
 * @author TANG
 * @since 2021/04/26
 */
@Mapper
public interface DistributedLockMapper extends BaseMapper<DistributedLock> {

    /**
     * 获取锁
     *
     * @param lock /
     * @return /
     */
    DistributedLock getLockByResourceName(DistributedLock lock);

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
}
