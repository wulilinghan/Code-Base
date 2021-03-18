package top.b0x0.demo.switchtooptimize.old;

import top.b0x0.demo.switchtooptimize.domain.CpaSourceEnum;
import top.b0x0.demo.switchtooptimize.domain.CpaUnionInfo;

/**
 * @author musui
 */
public class Test {


    private static Boolean callbackAdvertisers(CpaUnionInfo cpaUnionInfo) {
        switch (CpaSourceEnum.valueOf(cpaUnionInfo.getSource())) {
            case wangxiang:
                System.out.println("wangxiang... ");
                break;
            case jinritoutiao:
                System.out.println("jinritoutiao... ");
                break;
            case guangdiantong:
                System.out.println("guangdiantong... ");
                break;
            case tuia:
                System.out.println("tuia... ");
                break;
            case adjuz:
                System.out.println("adjuz... ");
                break;
            default:
        }
        return false;
    }

    public static void main(String[] args) {
        CpaUnionInfo cpaUnionInfo = new CpaUnionInfo();
        cpaUnionInfo.setSource("jinritoutiao");
//        CpaSourceEnum cpaSourceEnum = CpaSourceEnum.valueOf(cpaUnionInfo.getSource());
//        System.out.println("cpaSourceEnum = " + cpaSourceEnum);

        Boolean aBoolean = callbackAdvertisers(cpaUnionInfo);
        System.out.println("aBoolean = " + aBoolean);
    }
}
