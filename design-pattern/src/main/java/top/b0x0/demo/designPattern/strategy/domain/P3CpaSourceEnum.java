package top.b0x0.demo.designPattern.strategy.domain;

/**
 * @author musui
 */
public enum P3CpaSourceEnum {
    /**
     * 广告商
     */
    unknown(1, "未知", ""),
    guangdiantong(2, "广点通", "p3GuangdiantongCallBack"),
    wangxiang(3, "旺翔", "p3WangxiangCallBack"),
    jinritoutiao(4, "今日头条", "p3JinritoutiaoCallBack"),
    tuia(5, "推啊", "p3TuiaCallBack"),
    adjuz(6, "巨掌", "p3AdjuzCallBack");

    private final Integer id;
    private final String description;

    /**
     * SpringBean,用于找到对应的对象
     */
    private final String defaultBeanName;

    /**
     * @param id              /
     * @param description     /
     * @param defaultBeanName SpringBean,用于找到对应的对象
     */
    P3CpaSourceEnum(Integer id, String description, String defaultBeanName) {
        this.id = id;
        this.description = description;
        this.defaultBeanName = defaultBeanName;
    }


    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDefaultBeanName() {
        return defaultBeanName;
    }

    @Override
    public String toString() {
        return "CpaSourceEnum{" +
                "value=" + id +
                ", description='" + description + '\'' +
                ", defaultBeanName='" + defaultBeanName + '\'' +
                '}';
    }
}
