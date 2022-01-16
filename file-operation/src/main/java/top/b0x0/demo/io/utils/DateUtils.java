package top.b0x0.demo.io.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author Created By ManJiis 2022-01-15 18:28
 */
public class DateUtils {

    private static SimpleDateFormat getSdf(SdfEnum sdfEnum) {
        String patternPattern = sdfEnum.getPattern();
        SimpleDateFormat sdf = (SimpleDateFormat) ThreadLocalUtils.get(patternPattern);
        if (sdf == null) {
            sdf = new SimpleDateFormat(patternPattern);
            ThreadLocalUtils.put(patternPattern, sdf);
        }
        return sdf;
    }

    /**
     * @param date /
     * @return yyyy-MM-dd hh:mm:ss
     */
    public static String date2DateTimeStr(Date date) {
        return getSdf(SdfEnum.SDF_YMD_MHS).format(date);
    }

    /**
     * @param date /
     * @return yyyy-MM-dd
     */
    public static String date2DateStr(Date date) {
        return getSdf(SdfEnum.SDF_YMD).format(date);
    }

    /**
     * @param date /
     * @return yyyyMMddhhmmssS 20220115082534784
     */
    public static String date2TimestampStr(Date date) {
        return getSdf(SdfEnum.SDF_YMDMHS).format(date);
    }

}
