package top.b0x0.demo.circular.reference;

import org.springframework.beans.factory.annotation.Autowired;

class BeanA {

    @Autowired
    private BeanB beanB;

    public BeanB getBeanB() {
        return beanB;
    }

    public void setBeanB(BeanB beanB) {
        this.beanB = beanB;
    }
}

