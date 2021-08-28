package top.b0x0.demo.distributedLock.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.b0x0.demo.distributedLock.db.dao.DistributedLockMapper;
import top.b0x0.demo.distributedLock.domain.DistributedLock;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean tryLock(DistributedLock distributedLock) {
        int i = distributedLockMapper.insertLock(distributedLock);
        if (i > 0) {
            DistributedLock lock = distributedLockMapper.getLockByResource(distributedLock);
            return lock != null;
        }
        return false;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlock(String resource) {
        return distributedLockMapper.delByResource(resource) > 0;
    }

}