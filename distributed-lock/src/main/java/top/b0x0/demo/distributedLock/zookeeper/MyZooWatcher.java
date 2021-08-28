package top.b0x0.demo.distributedLock.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * @author musui
 */
public class MyZooWatcher implements Watcher {
    static ZooKeeper zooKeeper;

    static {
        try {
            zooKeeper = new ZooKeeper("127.0.0.1:2181", 4000, new MyZooWatcher());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("eventType:" + event.getType());
        if (event.getType() == Event.EventType.NodeDataChanged) {
            try {
                zooKeeper.exists(event.getPath(), true);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        String path = "/watcher";
        if (zooKeeper.exists(path, false) == null) {
            zooKeeper.create("/watcher", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        Thread.sleep(1000);
        System.out.println("-----------");
        //true表示使用zookeeper实例中配置的watcher
        Stat stat = zooKeeper.exists(path, true);
//        System.in.read();
        System.out.println("stat = " + stat);
    }
}