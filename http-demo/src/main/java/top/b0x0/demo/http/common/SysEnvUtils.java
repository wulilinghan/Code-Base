package top.b0x0.demo.http.common;

/**
 * @author TANG
 * @since 2021-04-08
 */
public class SysEnvUtils {
    public static final String SYS_WIN = "win";
    public static final String SYS_LINUX = "linux";
    public static String SYS_OS_NAME;

    static {
        SYS_OS_NAME = System.getProperty("os.name");
        System.out.println("SYS_OS = " + SYS_OS_NAME);
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
