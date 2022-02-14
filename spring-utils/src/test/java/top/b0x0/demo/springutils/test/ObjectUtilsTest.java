package top.b0x0.demo.springutils.test;

import org.junit.jupiter.api.Test;
import top.b0x0.demo.springutils.SpringUtilsApplication;

/**
 * @author ManJiis Created By 2022-02-14 22:19
 * @since 1.8
 */
public class ObjectUtilsTest extends SpringUtilsApplication {

    /**
     * 获取对象的基本信息
     * // 获取对象的类名。参数为 null 时，返回字符串："null"
     * String nullSafeClassName(Object obj)
     * // 参数为 null 时，返回 0
     * int nullSafeHashCode(Object object)
     * // 参数为 null 时，返回字符串："null"
     * String nullSafeToString(boolean[] array)
     * // 获取对象 HashCode（十六进制形式字符串）。参数为 null 时，返回 0
     * String getIdentityHexString(Object obj)
     * // 获取对象的类名和 HashCode。 参数为 null 时，返回字符串：""
     * String identityToString(Object obj)
     * // 相当于 toString()方法，但参数为 null 时，返回字符串：""
     * String getDisplayString(Object obj)
     */
    @Test
    void contextLoads() {
    }


    /**
     * 判断工具
     * // 判断数组是否为空
     * boolean isEmpty(Object[] array)
     * // 判断参数对象是否是数组
     * boolean isArray(Object obj)
     * // 判断数组中是否包含指定元素
     * boolean containsElement(Object[] array, Object element)
     * // 相等，或同为 null时，返回 true
     * boolean nullSafeEquals(Object o1, Object o2)
     *
     * <p><pre class="code">
     * 判断参数对象是否为空，判断标准为：
     *     Optional: Optional.empty()
     *        Array: length == 0
     * CharSequence: length == 0
     *  Collection: Collection.isEmpty()
     *          Map: Map.isEmpty()
     * </pre>
     * boolean isEmpty(Object obj)
     * <p>
     */
    @Test
    void contextLoads2() {
    }

    /**
     * // 向参数数组的末尾追加新元素，并返回一个新数组
     * <A, O extends A> A[] addObjectToArray(A[] array, O obj)
     * // 原生基础类型数组 --> 包装类数组
     * Object[] toObjectArray(Object source)
     * <p>
     */

    @Test
    void contextLoads3() {
    }
}
