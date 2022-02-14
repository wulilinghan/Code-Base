package top.b0x0.demo.springutils.test;

import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import top.b0x0.demo.springutils.SpringUtilsApplication;

/**
 * @author ManJiis Created By 2022-02-14 22:40
 * @since 1.8
 */
public class AopContextTest extends SpringUtilsApplication {

    @Test
    void contextLoads() {
        Object currentProxy = AopContext.currentProxy();
    }
}
