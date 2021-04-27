package top.b0x0.demo.distributedLock.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.b0x0.demo.distributedLock.db.dao.DistributedLockMapper;
import top.b0x0.demo.distributedLock.domain.DistributedLock;

import java.time.LocalDateTime;

/**
 * @author musui
 */
@Service
public class DistributedLockServiceImpl implements DistributedLockService {

    @Autowired
    DistributedLockMapper distributedLockMapper;

    /**
     * 锁状态
     */
    private static final String LOCK = "LOCK";
    private static final String UNLOCK = "UNLOCK";

    /**
     * 获取锁
     *
     * @param distributedLock /
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean tryLock(DistributedLock distributedLock) {
        DistributedLock lock = distributedLockMapper.getLockByResourceName(distributedLock);
        System.err.println("lock obj: " + lock);
        if (lock != null) {
            if (LOCK.equals(lock.getLockStatus())) {
                System.err.println("锁是否已失效: " + lock.getExpirationTime().isBefore(LocalDateTime.now()));
                if (lock.getExpirationTime().isBefore(LocalDateTime.now())) {
                    // 锁失效 ,更新锁状态
                    if (distributedLock.getExpirationTime() == null) {
                        // 加十分钟
                        distributedLock.setExpirationTime(LocalDateTime.now().plusMinutes(10));
                    }
                    System.err.println("锁失效 ,更新锁状态");
                    boolean b = distributedLockMapper.locked(distributedLock) > 0;
                    System.err.println("update = " + b);
                    return b;
                } else {
                    // 锁还在锁定期间
                    return false;
                }
            } else {
                return UNLOCK.equals(lock.getLockStatus());
            }
        }
        // TODO 未添加重试机制
        return false;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlock(DistributedLock distributedLock) {
        return distributedLockMapper.releaseLock(distributedLock) > 0;
    }

}