package top.b0x0.demo.infrastructure.switchtooptimize.p3_advancedop.inter;

import top.b0x0.demo.infrastructure.switchtooptimize.domain.CpaUnionInfo;

/**
 * 抽象策略角色
 *
 * @author musui
 */
public interface CpaCallBack {

    /**
     * 回调广告商
     *
     * @param cpaUnionInfo /
     * @return boolean
     */
    Boolean callbackAdvertisers(CpaUnionInfo cpaUnionInfo);
}
