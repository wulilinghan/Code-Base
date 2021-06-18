package top.b0x0.demo.circular.reference;

import org.springframework.beans.factory.annotation.Autowired;

class BeanB {

    @Autowired
    private BeanA beanA;

    public BeanA getBeanA() {
        return beanA;
    }

    public void setBeanA(BeanA beanA) {
        this.beanA = beanA;
    }
}