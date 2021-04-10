package top.b0x0.demo.http.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author TANG
 * @since 2021/04/10
 */
public class UserAuthUtils {

    public static final Auth USER_AUTH = new Auth();

    public static Map<String, String> genToken() {
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString().replace("-", "");
        Map<String, String> map = new HashMap<>(2);
        map.put("token", token);
        map.put("JSESSIONID", uuid.toString());
        USER_AUTH.set(map);
        return map;
    }

    public static void logout() {
        USER_AUTH.remove();
    }

    public static Boolean authToken(Map<String, String> tokenMap) {
        if (USER_AUTH.get() == null) {
            return false;
        }
        Set<String> keySet = tokenMap.keySet();
        boolean isToken = false;
        boolean isJsessionid = false;
        for (String s : keySet) {
            isToken = s.contains("token");
            isJsessionid = s.contains("JSESSIONID");
        }
        if (!isToken && !isJsessionid) {
            return false;
        }
        Map<String, String> stringMap = USER_AUTH.get();
        for (String key : stringMap.keySet()) {
            if (!stringMap.get(key).equals(tokenMap.get(key))) {
                return false;
            }
        }
        return true;
    }

}
