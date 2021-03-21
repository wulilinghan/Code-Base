package top.b0x0.demo.switchtooptimize.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.demo.switchtooptimize.domain.CpaUnionInfo;
import top.b0x0.demo.switchtooptimize.p2_newop.P2CallBack;
import top.b0x0.demo.switchtooptimize.p3_advancedop.P3CallBack;

/**
 * @author musui
 */
@RequestMapping("ad")
@RestController
public class CallBackController {
    @Autowired
    P2CallBack p2CallBack;

    @Autowired
    P3CallBack p3CallBack;

    @PostMapping("p2/callback")
    public void p2CallBack(CpaUnionInfo cpaUnionInfo) {
        p2CallBack.callBackActuator(cpaUnionInfo);
    }

    @PostMapping("p3/callback")
    public void p3CallBack(CpaUnionInfo cpaUnionInfo) {
        p3CallBack.callBackActuator(cpaUnionInfo);
    }

}
