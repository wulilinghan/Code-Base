package top.b0x0.demo.distributedLock.zookeeper.locks;


import cn.hutool.core.thread.ThreadUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 分布式可重入排它锁
 * <p>
 * curator 的几种锁方案 ：
 * <pre>
 *    1、InterProcessMutex：分布式可重入排它锁
 *    2、InterProcessSemaphoreMutex：分布式排它锁
 *    3、InterProcessReadWriteLock：分布式读写锁
 * </pre>
 *
 * @author musui
 */
public class InterProcessMutexLock {
    public static void main(String[] args) {
        CuratorFramework zkClient = getZkClient();
        String lockPath = "/lock";
        InterProcessMutex lock = new InterProcessMutex(zkClient, lockPath);

        //模拟多个个线程抢锁
        ThreadUtil.concurrencyTest(10, () -> {
            try {
                lock.acquire();
                System.out.println(System.currentTimeMillis() + "   " + Thread.currentThread().getName() + "线程  获取到了锁");
                //等到2秒后释放锁
                Thread.sleep(2000);
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


    private static CuratorFramework getZkClient() {
        String zkServerAddress = "127.0.0.1:2181";
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3, 5000);
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString(zkServerAddress)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        return zkClient;
    }
}