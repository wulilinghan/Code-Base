package top.b0x0.demo.springutils.test;

import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.AopContext;
import top.b0x0.demo.springutils.SpringUtilsApplication;

/**
 * @author ManJiis Created By 2022-02-14 22:40
 * @since 1.8
 */
public class AopUtilsTest extends SpringUtilsApplication {
    /**
     * // 判断是不是 Spring 代理对象
     * boolean isAopProxy()
     * // 判断是不是 jdk 动态代理对象
     * isJdkDynamicProxy()
     * // 判断是不是 CGLIB 代理对象
     * boolean isCglibProxy()
     * <p>
     * // 获取被代理的目标 class
     * Class<?> getTargetClass()
     */
    @Test
    void contextLoads() {

    }
}
