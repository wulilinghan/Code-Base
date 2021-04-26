package top.b0x0.demo.distributedLock.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.b0x0.demo.distributedLock.domain.DistributedLock;

import java.time.LocalDateTime;

/**
 * @author musui
 */
@Service
public class DistributedLockService {

    @Autowired
    private DistributedLockMapper distributedLockMapper;

    /**
     * 锁状态
     */
    private static final String LOCK_STATUS_LOCK = "LOCK";
    private static final String LOCK_STATUS_UNLOCK = "UNLOCK";

    /**
     * 获取锁
     *
     * @param distributedLock /
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean tryLock(DistributedLock distributedLock) {
        DistributedLock lock = distributedLockMapper.getLockByResourceName(distributedLock);
        if (lock != null) {
            if (LOCK_STATUS_LOCK.equals(lock.getLockStatus())) {
                if (lock.getExpirationTime().isBefore(LocalDateTime.now())) {
                    // 锁失效 ,更新锁状态
                    if (distributedLock.getExpirationTime() == null) {
                        distributedLock.setExpirationTime(LocalDateTime.now().plusMinutes(10));
                    }
                    return distributedLockMapper.locked(distributedLock) > 0;
                } else {
                    return false;
                }
            }
        }
        // TODO 未添加重试机制
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean unlock(DistributedLock distributedLock) {
        DistributedLock result = distributedLockMapper.getLockByResourceName(distributedLock);
        if (result != null) {
            return distributedLockMapper.releaseLock(distributedLock) > 0;
        } else {
            return false;
        }
    }

}