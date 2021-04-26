package top.b0x0.demo.distributedLock.zookeeper.locks;


import cn.hutool.core.thread.ThreadUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import top.b0x0.demo.distributedLock.zookeeper.util.ZkUtils;

/**
 * 分布式可重入排它锁
 * <p>
 * curator 的几种锁方案 ：
 * <pre>
 *    1、InterProcessMutex：分布式可重入排它锁
 *    2、InterProcessSemaphoreMutex：分布式不可重入排它锁
 *    3、InterProcessReadWriteLock：分布式可重入读写锁
 * </pre>
 *
 * @author musui
 */
public class InterProcessMutexLock {
    public static void main(String[] args) {
        CuratorFramework zkClient = ZkUtils.getZkClient();
        // 以商户号为锁，防止主子账户同时交易，额度扣减异常
        String merId = "0755-202104250011";
        String lockPath = "/lock/" + merId;
        // 可重入排它锁
        InterProcessMutex lock = new InterProcessMutex(zkClient, lockPath);

        //模拟多个个线程抢锁
        ThreadUtil.concurrencyTest(5, () -> {
            try {
                lock.acquire();
                System.out.println(System.currentTimeMillis() + "   " + Thread.currentThread().getName() + "线程  获取到了锁");
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println(System.currentTimeMillis() + "   " + Thread.currentThread().getName() + " 线程  释放锁");
                try {
                    // 释放锁
                    lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}