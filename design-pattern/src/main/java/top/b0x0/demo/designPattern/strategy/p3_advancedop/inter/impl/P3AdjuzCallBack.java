package top.b0x0.demo.designPattern.strategy.p3_advancedop.inter.impl;

import org.springframework.stereotype.Component;
import top.b0x0.demo.designPattern.strategy.domain.CpaUnionInfo;
import top.b0x0.demo.designPattern.strategy.p3_advancedop.inter.CpaCallBack;

/**
 * 具体策略角色，以今日头条回调为例，其它类似。
 *
 * @author musui
 */
@Component
public class P3AdjuzCallBack implements CpaCallBack {

    @Override
    public Boolean callbackAdvertisers(CpaUnionInfo cpaUnionInfo) {
        System.out.println("p3 --> P3AdjuzCallBack = " + cpaUnionInfo);
        return true;
    }
}
