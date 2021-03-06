//package top.b0x0.demo.distributedLock.zookeeper.locks;
//
//import cn.hutool.core.thread.ThreadUtil;
//import org.I0Itec.zkclient.IZkDataListener;
//import org.I0Itec.zkclient.ZkClient;
//import org.I0Itec.zkclient.exception.ZkNodeExistsException;
//
//import java.util.concurrent.CountDownLatch;
//
///**
// * zkTest
// *
// * @author musui
// * @since 2020-04-06
// **/
//public class ZkTest {
//    static int inventory = 10;
//    private static final int NUM = 10;
//    private static CountDownLatch cdl = new CountDownLatch(1);
//
//    private static final String IP_PORT = "127.0.0.1:2181";
//    private static final String Z_NODE = "/LOCK";
//
//    private static ZkClient zkClient = new ZkClient(IP_PORT);
//
//
//    public static void main(String[] args) {
//        try {
//            ThreadUtil.concurrencyTest(10, () -> {
//                ZkTest zkTest = new ZkTest();
//                try {
//                    zkTest.lock();
//                    if (inventory > 0) {
//                        inventory--;
//                    }
//                    System.out.println(inventory);
//                } finally {
//                    zkTest.unlock();
//                    System.out.println("释放锁");
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void lock() {
//        // 尝试加锁
//        if (tryLock()) {
//            return;
//        }
//        // 进入等待 监听
//        waitForLock();
//        // 再次尝试
//        lock();
//    }
//
//    public boolean tryLock() {
//        try {
//            zkClient.createPersistent(Z_NODE);
//            return true;
//        } catch (ZkNodeExistsException e) {
//            return false;
//        }
//    }
//
//    public void unlock() {
//        zkClient.delete(Z_NODE);
//    }
//
//    public void waitForLock() {
//        System.out.println("加锁失败");
//        IZkDataListener listener = new IZkDataListener() {
//            @Override
//            public void handleDataChange(String s, Object o) throws Exception {
//
//            }
//
//            @Override
//            public void handleDataDeleted(String s) throws Exception {
//                System.out.println("唤醒");
//                cdl.countDown();
//            }
//        };
//        // 监听
//        zkClient.subscribeDataChanges(Z_NODE, listener);
//        if (zkClient.exists(Z_NODE)) {
//            try {
//                cdl.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        // 释放监听
//        zkClient.unsubscribeDataChanges(Z_NODE, listener);
//    }
//
//}
