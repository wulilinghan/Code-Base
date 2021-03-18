package top.b0x0.demo.switchtooptimize.domain;

/**
 * @author musui
 */

public enum CpaSourceEnum {
    /**
     * 广告商
     */
    unknown(1, "未知", ""),
    guangdiantong(2, "广点通", "guangdiantongCallBack"),
    wangxiang(3, "旺翔", "wangxiangCallBack"),
    jinritoutiao(4, "今日头条", "jinritoutiaoCallBack"),
    tuia(5, "推啊", "tuiaCallBack"),
    adjuz(6, "巨掌", "adjuzCallBack");

    private int value;
    private String description;

    /**
     * SpringBean,用于找到对应的对象
     */
    private String defaultBeanName;

    /**
     * @param value           /
     * @param description     /
     * @param defaultBeanName SpringBean,用于找到对应的对象
     */
    CpaSourceEnum(int value, String description, String defaultBeanName) {
        this.value = value;
        this.description = description;
        this.defaultBeanName = defaultBeanName;
    }

    public String getDefaultBeanName() {
        return defaultBeanName;
    }
}
