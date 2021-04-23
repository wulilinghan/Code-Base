package top.b0x0.demo.redis;

import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.demo.redis.domain.R;
import top.b0x0.demo.redis.service.IRedisService;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static top.b0x0.demo.redis.domain.RedisKey.ORDER_LOCK_KEY;

/**
 * @author TANG
 * @since 2021-04-23
 */
@RestController
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
            Boolean tryLock = redisService.tryLock(ORDER_LOCK_KEY, orderId, 5, TimeUnit.SECONDS);
            System.out.println(String.format("ms: %s, order: %s , lock %s.", System.currentTimeMillis(), orderId, tryLock));
        });

        ExecutorService executorService = new ThreadPoolExecutor(20,
                100,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(Integer.MAX_VALUE));
        executorService.execute(()->{

        });
        // 获取总的执行时间，单位毫秒
        return R.ok(concurrencyTest.getInterval() + "ms");
    }
}
