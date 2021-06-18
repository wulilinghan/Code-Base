package top.b0x0.demo.circular.reference;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 循环引用测试
 *
 * @author TANG
 * @since 2021/05/19
 */
public class CircularReferenceTest {

    public static void main(String[] args) {
        // 通过AnnotationConfigApplicationContext创建了IOC容器，并先后注册了BeanA和BeanB，BeanA和BeanB相互依赖
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanA.class, BeanB.class);
        BeanA beanA = context.getBean(BeanA.class);
        BeanB beanB = context.getBean(BeanB.class);
        BeanB beanBInBeanA = beanA.getBeanB();
        BeanA beanAInBeanB = beanB.getBeanA();
        System.out.println(beanA);
        System.out.println(beanB);
        System.out.println(beanB == beanBInBeanA);
        System.out.println(beanA == beanAInBeanB);
    }
}
