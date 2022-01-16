package top.b0x0.demo.io.utils;

/**
 * SimpleDateFormat 格式枚举定义
 *
 * @author Created By ManJiis 2022-01-15 18:28
 */
public enum SdfEnum {
    /**
     * SimpleDateFormat 格式枚举定义
     */
    SDF_YMD("yyyy-MM-dd"),
    SDF_YMD_MHS("yyyy-MM-dd hh:mm:ss"),
    SDF_YMDMHS("yyyyMMddhhmmssS"),
    ;

    private final String pattern;

    SdfEnum(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
