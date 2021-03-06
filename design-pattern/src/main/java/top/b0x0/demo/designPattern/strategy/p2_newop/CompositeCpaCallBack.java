package top.b0x0.demo.designPattern.strategy.p2_newop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import top.b0x0.demo.designPattern.strategy.domain.CpaUnionInfo;
import top.b0x0.demo.designPattern.strategy.domain.P2CpaSourceEnum;
import top.b0x0.demo.designPattern.strategy.p2_newop.abstractClazz.AbstractCpaCallBack;

/**
 * @author musui
 */
@Component
public class CompositeCpaCallBack implements ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public AbstractCpaCallBack getCpaCallBack(String source) {
        P2CpaSourceEnum cpaSourceEnum = P2CpaSourceEnum.valueOf(source);
        return (AbstractCpaCallBack) applicationContext.getBean(cpaSourceEnum.getDefaultBeanName());
    }

    public Boolean callbackAdvertisers(CpaUnionInfo cpaUnionInfo) {
        try {
            AbstractCpaCallBack callBack = getCpaCallBack(cpaUnionInfo.getSource());
            return callBack.callbackAdvertisers(cpaUnionInfo);
        } catch (Exception e) {
            log.error("p2 --> 回调异常,{}", e.getMessage());
            return false;
        }
    }


}
