package top.b0x0.demo.http.common;

import java.util.Map;

/**
 * @author TANG
 * @since 2021-04-08
 */
public class SysEnvUtils {
    public static final String SYS_WIN = "win";
    public static final String SYS_LINUX = "linux";
    public static String SYS_OS;

    static {
        SYS_OS = System.getenv("OS");
    }

    public static void main(String[] args) {
//        Map<String, String> envMap = System.getenv();
//        for (String key : envMap.keySet()) {
//            String val = envMap.get(key);
//            String x = key + " = " + val;
//            System.out.println(x);
//        }
//        System.out.println("System.getProperty(\"OS\") = " + System.getProperty("OS"));
//        System.out.println("System.getenv(\"OS\") = " + System.getenv("OS"));
    }
}
