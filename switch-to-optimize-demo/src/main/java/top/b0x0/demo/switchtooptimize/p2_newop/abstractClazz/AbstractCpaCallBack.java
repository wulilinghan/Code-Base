package top.b0x0.demo.switchtooptimize.p2_newop.abstractClazz;

import top.b0x0.demo.switchtooptimize.domain.CpaUnionInfo;

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
