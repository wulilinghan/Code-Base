package top.b0x0.demo.distributedLock.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


/**
 * 使用 zookeeper 原生 API 连接
 *
 * @author musui
 */
public class ConnectionDemo {

    public static void main(String[] args) {
        // 使用 zookeeper 原生 API, 连接集群, 因为连接需要时间，用 countDownLatch 阻塞，等待连接成功，控制台输出连接状态！
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", 4000, new Watcher() {
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
            Stat exists = zooKeeper.exists("/top.b0x0.demo", false);
            if (exists != null) {
                System.out.println("exists = " + exists.toString());
            } else {
                zooKeeper.create("/top.b0x0.demo", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException | InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }
}
