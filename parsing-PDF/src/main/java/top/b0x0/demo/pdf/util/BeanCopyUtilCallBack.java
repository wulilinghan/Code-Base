package top.b0x0.demo.pdf.util;

/**
 * @author TAGN
 * @since 2021-03-22
 */
@FunctionalInterface
public interface BeanCopyUtilCallBack<S, T> {

    /**
     * 定义默认回调方法
     *
     * @param t /
     * @param s /
     */
    void callBack(S t, T s);
}