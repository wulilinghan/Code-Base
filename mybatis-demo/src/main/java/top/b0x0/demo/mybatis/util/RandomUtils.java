package top.b0x0.demo.mybatis.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * https://www.cnblogs.com/gocode/p/extract-unique-and-random-elements-in-array.html
 *
 * @author musui
 */
public class RandomUtils {
    private static final Logger log = LoggerFactory.getLogger(RandomUtils.class);


    private static final Random RANDOM;

    static {
        RANDOM = new Random();
    }

    /**
     * 从List集合中随机抽取指定数量的非重复元素
     * <p>注意：集合中不能有null元素,否则返回值中可能有重复的元素
     *
     * @param src         List集合源
     * @param chooseCount 抽取的元素个数
     * @return /
     * @see [类、类#方法、类#成员]
     */
    public static <T> List<T> randomChooseElements(List<T> src, int chooseCount) {
        return randomChooseElements(src, chooseCount, null);
    }

    /**
     * 从List集合中随机抽取指定数量的非重复元素
     *
     * @param src          List集合源
     * @param chooseCount  抽取的元素个数
     * @param srcNoContain 集合源中不包含的任意对象
     * @return /
     * @see [类、类#方法、类#成员]
     */
    public static <T> List<T> randomChooseElements(List<T> src, int chooseCount, T srcNoContain) {
        for (Object element : src) {
            if (element == srcNoContain) {
                throw new IllegalStateException("指定的不同元素srcNoContain与参数src中的某一个元素相同");
            }
        }

        if (chooseCount > src.size()) {
            throw new IllegalArgumentException("参数chooseCount不能大于集合src的元素个数.");
        }

        int sizeOfCopiedList = src.size();
        List<T> copiedList = new ArrayList<T>(src);

        List<T> choosedList = new ArrayList<T>();
        int index = -1;
        for (int i = 0; i < chooseCount; i++) {
            while (true) {
                index = RANDOM.nextInt(sizeOfCopiedList);
                if (copiedList.get(index) != srcNoContain) {
                    choosedList.add(copiedList.get(index));
                    copiedList.set(index, srcNoContain);
                    break;
                }
            }
        }

        return choosedList;

    }

    /**
     * 从数组中随机抽取指定数量的非重复元素
     * <p>注意：数组中不能有null元素,否则返回值中可能有重复的元素
     *
     * @param src         数组源
     * @param chooseCount 抽取的元素个数
     * @return /
     * @see [类、类#方法、类#成员]
     */
    public static Object[] randomChooseElements(Object[] src, int chooseCount) {
        return randomChooseElements(src, chooseCount, null);
    }

    /**
     * 从数组中随机抽取指定数量的非重复元素
     *
     * @param src          数组源
     * @param chooseCount  抽取的元素个数
     * @param srcNoContain 源数组不包含的（类类型与数组的元素类型相同）任意对象
     * @return /
     * @see [类、类#方法、类#成员]
     */
    public static Object[] randomChooseElements(Object[] src, int chooseCount, Object srcNoContain) {
        for (Object element : src) {
            if (element == srcNoContain) {
                throw new IllegalStateException("指定的不同元素srcNoContain与参数src中的某一个元素相同");
            }
        }
        if (chooseCount > src.length) {
            throw new IllegalArgumentException("参数chooseCount不能大于数组参数src的长度.");
        }

        Object[] copiedArray = Arrays.copyOf(src, src.length);

        Object[] choosedArray = new Object[chooseCount];
        int index = -1;
        for (int i = 0; i < choosedArray.length; i++) {
            while (true) {
                index = RANDOM.nextInt(copiedArray.length);
                if (copiedArray[index] != srcNoContain) {
                    choosedArray[i] = copiedArray[index];
                    copiedArray[index] = srcNoContain;
                    break;
                }
            }
        }
        return choosedArray;
    }


    /**
     * 在[beginStr,endStr]生成随机日期+时间
     *
     * @param beginStr / ex: 2018-11-27 10:00:00
     * @param endStr   / ex: 2018-11-28 12:00:00
     * @return /
     */
    public static DateTime randomDateTime(String beginStr, String endStr) {
        try {

            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            return getDateTime(beginStr, endStr, format);
        } catch (Exception e) {
            log.error("randomDateTime error : {}", e.getMessage());
        }
        return null;
    }


    /**
     * 在[beginStr,endStr]只随机生成时间,不包含日期
     * 在没有添加日期格式时,默认为1970-01-01
     *
     * @param beginStr / ex: 10:00:00
     * @param endStr   / ex: 12:00:00
     * @return /
     */
    public static DateTime randomTime(String beginStr, String endStr) {
        try {
            DateTimeFormatter format = DateTimeFormat.forPattern("HH:mm:ss");
            return getDateTime(beginStr, endStr, format);
        } catch (Exception e) {
            log.error("randomTime error : {}", e.getMessage());
        }
        return null;
    }

    private static DateTime getDateTime(String beginStr, String endStr, DateTimeFormatter format) {
        DateTime beginTime = DateTime.parse(beginStr, format);
        DateTime endTime = DateTime.parse(endStr, format);

        if (beginTime.getMillis() > endTime.getMillis()) {
            return null;
        }

        long randDateTime = random(beginTime.getMillis(), endTime.getMillis());
        return new DateTime(randDateTime);
    }


    /**
     * 随机生成时间
     * 使用Math.random()方法---返回一个在介于(0,1)的随机数
     *
     * @param begin /
     * @param end   /
     * @return /
     */
    private static long random(long begin, long end) {
        long rand = begin + (long) (Math.random() * (end - begin));
        if (rand == begin || rand == end) {
            return random(begin, end);
        }
        return rand;
    }

    public static void main(String[] args) throws ParseException {
        List<Date> dates1 = Arrays.asList(new Date(119, 7, 21),
                new Date(119, 3, 12),
                new Date(119, 9, 7),
                new Date(119, 3, 23));

        List<Date> selectDates = randomChooseElements(dates1, 3);
        System.out.println("源集合是：" + dates1);
        System.out.println("集合中随机抽取的元素：" + selectDates);


        System.out.println();
        Date[] dates = new Date[]{new Date(119, 7, 21), new Date(119, 3, 12), new Date(119, 9, 7),
                new Date(119, 3, 23)};
        Object[] arr1 = randomChooseElements(dates, 2);
        System.out.println("源数组是" + Arrays.toString(dates));
        System.out.println("数组中随机选择出来元素" + Arrays.toString(arr1));
//-----------------------------------------------------------------------------------------------------------------------
        //随机生成日期和时间
        DateTime randomDateTime = randomDateTime("2018-11-27 10:00:00", "2018-11-28 12:00:00");
        System.out.println(randomDateTime);
        //DateTime ---> String
        String dateStr = randomDateTime.toString("yyyy-MM-dd HH:mm:ss");
        System.out.println(dateStr);
        System.out.println("-----------------------------------------");


        //只随机生成当天固定时间段的时间(日期+时间)
        DateTime day = new DateTime();
        //System.out.println(day);
        DateTime randomDateTime2 = randomDateTime(day.toString("yyyy-MM-dd") + " 06:00:00", day.toString("yyyy-MM-dd") + " 11:00:00");
        System.out.println(randomDateTime2);
        //DateTime ---> String --->Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = sdf.parse(randomDateTime2.toString("yyyy-MM-dd HH:mm:ss"));
        System.out.println(date2);
        System.out.println("-----------------------------------------");

        //只随机生成时间
        DateTime randomTime = randomTime("10:00:00", "12:00:00");
        System.out.println(randomTime);//没有添加日期,则默认为"1970-01-01"
        System.out.println(randomTime.toString().substring(11, 19));
    }
}