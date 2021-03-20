package top.b0x0.demo.switchtooptimize.newop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import top.b0x0.demo.switchtooptimize.domain.CpaSourceEnum;
import top.b0x0.demo.switchtooptimize.domain.CpaUnionInfo;

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

    public CpaCallBack getCpaCallBack(String source) {
        CpaSourceEnum cpaSourceEnum = CpaSourceEnum.valueOf(source);
        return (CpaCallBack) applicationContext.getBean(cpaSourceEnum.getDefaultBeanName());
    }

    public Boolean callbackAdvertisers(CpaUnionInfo cpaUnionInfo) {
        try {
            CpaCallBack callBack = getCpaCallBack(cpaUnionInfo.getSource());
            return callBack.callbackAdvertisers(cpaUnionInfo);
        } catch (Exception e) {
            log.error("回调异常,{}", e.getMessage());
            return false;
        }
    }


}
