package top.b0x0.demo.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.b0x0.demo.infrastructure.pdf.config.ExecutorConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@RunWith(SpringRunner.class)
@SpringBootTest
class PdfDemoApplicationTests {

    @Autowired
    ExecutorConfig executorConfig;

    private static final ExecutorService SERVICE = Executors.newFixedThreadPool(10);

    @Test
    void contextLoads() {
/*        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            try {
                latch.await();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                latch.countDown();
            }
        }*/
    }

}
