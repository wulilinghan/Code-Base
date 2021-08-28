package top.b0x0.demo.designPattern.strategy.p1_oldop;

import top.b0x0.demo.designPattern.strategy.domain.CpaUnionInfo;
import top.b0x0.demo.designPattern.strategy.domain.P1CpaSourceEnum;

/**
 * @author musui
 */
public class Test {

    private static Boolean callbackAdvertisers(CpaUnionInfo cpaUnionInfo) {
        switch (P1CpaSourceEnum.valueOf(cpaUnionInfo.getSource())) {
            case wangxiang:
                System.out.println("wangxiang... ");
                return true;
            case jinritoutiao:
                System.out.println("jinritoutiao... ");
                return true;
            case guangdiantong:
                System.out.println("guangdiantong... ");
                return true;
            case tuia:
                System.out.println("tuia... ");
                return true;
            case adjuz:
                System.out.println("adjuz... ");
                return true;
            default:
        }
        return false;
    }

    public static void main(String[] args) {
        CpaUnionInfo cpaUnionInfo = new CpaUnionInfo();
        cpaUnionInfo.setSource("jinritoutiao");
        P1CpaSourceEnum p1CpaSourceEnum = P1CpaSourceEnum.valueOf(cpaUnionInfo.getSource());
        System.out.println("cpaSourceEnum.valueOf = " + p1CpaSourceEnum);

        Boolean aBoolean = callbackAdvertisers(cpaUnionInfo);
        System.out.println("aBoolean = " + aBoolean);
    }
}
