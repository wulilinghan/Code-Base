package top.b0x0.demo.switchtooptimize;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.b0x0.demo.switchtooptimize.domain.P1CpaSourceEnum;
import top.b0x0.demo.switchtooptimize.domain.CpaUnionInfo;
import top.b0x0.demo.switchtooptimize.p2_newop.CompositeCpaCallBack;

@SpringBootTest
class DemoSwitchToOptimizeApplicationTests {

    @Autowired
    private CompositeCpaCallBack compositeCpaCallBack;

    @Test
    void enumTest() {
        P1CpaSourceEnum[] values = P1CpaSourceEnum.values();
        for (P1CpaSourceEnum value : values) {
            System.out.println("value = " + value);
        }
        int length = values.length;
        System.out.println("length = " + length);
        P1CpaSourceEnum unknown = P1CpaSourceEnum.valueOf("unknown");
        System.out.println("unknown = " + unknown);
    }

    @Test
    void contextLoads() {
        CpaUnionInfo cpaUnionInfo = new CpaUnionInfo();
        cpaUnionInfo.setSource("jinritoutiao");
        Boolean aBoolean = compositeCpaCallBack.callbackAdvertisers(cpaUnionInfo);
        System.out.println("aBoolean = " + aBoolean);

        cpaUnionInfo.setSource("adjuz");
        Boolean aBoolean2 = compositeCpaCallBack.callbackAdvertisers(cpaUnionInfo);
        System.out.println("aBoolean2 = " + aBoolean2);

    }

}
