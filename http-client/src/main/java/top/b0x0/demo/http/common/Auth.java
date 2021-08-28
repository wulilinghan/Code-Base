package top.b0x0.demo.http.common;

import java.util.Map;

/**
 * @author TANG
 * @since 2021/04/10
 */
public class Auth {
    private Map<String, String> auth;

    public void set(Map<String, String> auth) {
        this.auth = auth;
    }

    public Map<String, String> get() {
        return this.auth;
    }

    public void remove() {
        this.auth = null;
    }

}
