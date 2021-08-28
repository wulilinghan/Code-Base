package top.b0x0.demo.designPattern.strategy.p2_newop.abstractClazz;

import top.b0x0.demo.designPattern.strategy.domain.CpaUnionInfo;

/**
 * 抽象策略角色
 *
 * @author musui
 */
public abstract class AbstractCpaCallBack {

    /**
     * 回调广告商
     *
     * @param cpaUnionInfo /
     * @return boolean
     */
    public abstract Boolean callbackAdvertisers(CpaUnionInfo cpaUnionInfo);
}
