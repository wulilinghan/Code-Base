package top.b0x0.demo.redis;

import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.demo.redis.domain.R;
import top.b0x0.demo.redis.service.IRedisService;

import java.util.concurrent.TimeUnit;

import static top.b0x0.demo.redis.domain.RedisKey.ORDER_LOCK_KEY;

/**
 * @author TANG
 * @since 2021-04-23
 */
@Slf4j
@RestController
@RequestMapping("lock")
public class Controller {

    @Autowired
    IRedisService redisService;

    @GetMapping("t1")
    public R t1(Integer threadNum) {
        System.out.println("threadNum = " + threadNum);
        if (threadNum == null) {
            threadNum = 500;
        }
        String orderId = "202104231736";
        ConcurrencyTester concurrencyTest = ThreadUtil.concurrencyTest(threadNum, () -> {
            orderTrade(orderId);
        });

        // 获取总的执行时间，单位毫秒
        return R.ok(concurrencyTest.getInterval() + "ms");
    }

    @GetMapping("t2")
    public R t2() {
        String orderId = "202104231736";
        orderTrade(orderId);
        return R.ok();
    }

    private void orderTrade(String orderId) {
        String machineId = "1";
        try {
            Boolean tryLock = redisService.tryLock(String.format(ORDER_LOCK_KEY, machineId), orderId, 30, TimeUnit.SECONDS);
            if (Boolean.TRUE.equals(tryLock)) {
                System.out.printf("currentTimeMillis: [%s], 订单号: [%s] ---------上锁成功-------- %n", System.currentTimeMillis(), orderId);
            } else {
                System.out.printf("currentTimeMillis: [%s], 订单号: [%s] 已被锁定 %n", System.currentTimeMillis(), orderId);
            }
        } catch (Exception e) {
            log.error("订单: [{}] 交易异常 信息:{}", orderId, e.getMessage());
        }
    }
}
