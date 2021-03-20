package top.b0x0.demo.switchtooptimize.newop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.b0x0.demo.switchtooptimize.domain.CpaUnionInfo;

/**
 * 抽象策略角色
 *
 * @author musui
 */
public abstract class CpaCallBack {

    public Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 回调广告商
     *
     * @param cpaUnionInfo /
     * @return boolean
     */
    abstract Boolean callbackAdvertisers(CpaUnionInfo cpaUnionInfo);
}
