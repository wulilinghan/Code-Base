package top.b0x0.demo.designPattern.strategy.p3_advancedop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import top.b0x0.demo.designPattern.strategy.domain.CpaUnionInfo;
import top.b0x0.demo.designPattern.strategy.domain.P3CpaSourceEnum;
import top.b0x0.demo.designPattern.strategy.p3_advancedop.inter.CpaCallBack;

import java.util.List;
import java.util.Map;

/**
 * @author musui
 */
@Component
public class P3CallBack {
    private static final Logger log = LoggerFactory.getLogger(P3CallBack.class);
    /**
     * spring会自动将 CpaCallBack 的所有实现类bean注入到list集合
     */
    @Autowired
    private List<CpaCallBack> cpaCallBackList;

    /**
     * 通过Map注入，通过 spring bean 的名称作为key动态获取对应实例
     */
    @Autowired
    private Map<String, CpaCallBack> cpaCallBackMap;

    public void callBackActuator(CpaUnionInfo cpaUnionInfo) {
        P3CpaSourceEnum p3CpaSourceEnum = P3CpaSourceEnum.valueOf(cpaUnionInfo.getSource());
        CpaCallBack cpaCallBack = cpaCallBackMap.get(p3CpaSourceEnum.getDefaultBeanName());
        try {
            Assert.notNull(cpaCallBack, "No bean named '" + p3CpaSourceEnum.getDefaultBeanName() + "'");
            cpaCallBack.callbackAdvertisers(cpaUnionInfo);
        } catch (Exception e) {
            log.error("p3 --> 回调异常,{}", e.getMessage());
        }
    }
}
