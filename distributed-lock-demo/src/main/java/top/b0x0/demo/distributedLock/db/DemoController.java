package top.b0x0.demo.distributedLock.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.demo.distributedLock.domain.DistributedLock;

/**
 * @author musui
 */
@RestController
public class DemoController {
    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private DistributedLockService distributedLockService;


    @GetMapping("mysqlLock")
    public String testLock() throws Exception {
        log.debug("进入testLock()方法;");
        DistributedLock lock = new DistributedLock();
        lock.setResourceName("task-order");
        boolean tryLock = distributedLockService.tryLock(lock);
        if (tryLock) {
            log.debug("获取到分布式锁；");
            Thread.sleep(10 * 1000);
        } else {
            log.debug("获取分布式锁失败；");
            throw new Exception("获取分布式锁失败；");
        }
        log.debug("执行完成；");
        return "返回结果";
    }

}