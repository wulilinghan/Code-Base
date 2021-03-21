package top.b0x0.demo.switchtooptimize.p2_newop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.b0x0.demo.switchtooptimize.domain.CpaUnionInfo;

/**
 * @author musui
 */
@Component
public class P2CallBack {

    @Autowired
    private CompositeCpaCallBack compositeCpaCallBack;

    public void callBackActuator(CpaUnionInfo cpaUnionInfo) {
        compositeCpaCallBack.callbackAdvertisers(cpaUnionInfo);
    }
}
