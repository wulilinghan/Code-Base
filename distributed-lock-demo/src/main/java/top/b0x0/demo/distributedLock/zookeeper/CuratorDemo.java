package top.b0x0.demo.distributedLock.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

/**
 * 使用Curator连接zookeeper
 *
 * @author musui
 */
public class CuratorDemo {

    public static void main(String[] args) throws Exception {
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181," + "127.0.0.1:2182" + "127.0.0.1:2183")
                .sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("").build();
        curator.start();
        Stat stat = new Stat();
        byte[] bytes = curator.getData().storingStatIn(stat).forPath("/top.b0x0.demo");
        System.out.println(new String(bytes));
        curator.close();
    }
}
