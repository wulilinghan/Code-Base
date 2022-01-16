package top.b0x0.demo.io.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * ThreadLocalUtils
 *
 * @author Created By ManJiis 2022-01-15 18:28
 */
public class ThreadLocalUtils {

    private ThreadLocalUtils() {
    }

    private final static ThreadLocal<Map<String, Object>> THREAD_CONTEXT = ThreadLocal.withInitial(
            () -> new HashMap<>(8)
    );

    public static Object get(String key) {
        // getContextMap()表示要先获取THREAD_CONTEXT的value，也就是Map<String, Object>。然后再从Map<String, Object>中根据key获取
        return THREAD_CONTEXT.get().get(key);
    }


    public static void put(String key, Object value) {
        THREAD_CONTEXT.get().put(key, value);
    }


    public static Object remove(String key) {
        return THREAD_CONTEXT.get().remove(key);
    }


    public static void clear() {
        THREAD_CONTEXT.get().clear();
    }


    public static void clearAll() {
        THREAD_CONTEXT.remove();
    }

}