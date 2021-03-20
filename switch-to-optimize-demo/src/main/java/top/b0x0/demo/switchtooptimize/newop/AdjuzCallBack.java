package top.b0x0.demo.switchtooptimize.newop;

import org.springframework.stereotype.Component;
import top.b0x0.demo.switchtooptimize.domain.CpaUnionInfo;

/**
 * 具体策略角色，以今日头条回调为例，其它类似。
 *
 * @author musui
 */
@Component
public class AdjuzCallBack extends CpaCallBack {

    @Override
    public Boolean callbackAdvertisers(CpaUnionInfo cpaUnionInfo) {
        System.out.println("调用巨掌回调方法 = " + cpaUnionInfo);
        return true;
    }
}
