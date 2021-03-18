package top.b0x0.demo.switchtooptimize.newop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.b0x0.demo.switchtooptimize.domain.CpaUnionInfo;

/**
 * @author musui
 */
@Component
public class NewTest {
    @Autowired
    private CompositeCpaCallBack compositeCpaCallBack;

    public static void main(String[] args) {
        CpaUnionInfo cpaUnionInfo = new CpaUnionInfo();
        cpaUnionInfo.setSource("jinritoutiao");
    }
}
