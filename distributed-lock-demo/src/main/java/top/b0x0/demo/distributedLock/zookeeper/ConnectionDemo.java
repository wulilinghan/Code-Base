package top.b0x0.demo.distributedLock.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


/**
 * @author musui
 */
public class ConnectionDemo {

    public static void main(String[] args) {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 4000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    //如果收到了服务端的响应事件，连接成功
                    if (Event.KeeperState.SyncConnected == event.getState()) {
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            System.out.println(zooKeeper.getState());
            zooKeeper.create("/top.b0x0.demo", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (IOException | InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }
}
