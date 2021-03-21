package top.b0x0.demo.switchtooptimize.p2_newop.abstractClazz.impl;

import org.springframework.stereotype.Component;
import top.b0x0.demo.switchtooptimize.domain.CpaUnionInfo;
import top.b0x0.demo.switchtooptimize.p2_newop.abstractClazz.CpaCallBack;

/**
 * 具体策略角色，以今日头条回调为例，其它类似。
 *
 * @author musui
 */
@Component
public class P2AdjuzCallBack extends CpaCallBack {

    @Override
    public Boolean callbackAdvertisers(CpaUnionInfo cpaUnionInfo) {
        System.out.println("p2 --> P2AdjuzCallBack = " + cpaUnionInfo);
        return true;
    }
}
