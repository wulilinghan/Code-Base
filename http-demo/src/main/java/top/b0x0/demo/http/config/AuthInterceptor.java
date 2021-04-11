package top.b0x0.demo.http.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import top.b0x0.demo.http.common.R;
import top.b0x0.demo.http.common.UserAuthUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author TANG
 * @since 2021/04/11
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        R notAuth = R.notAuth();
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("token");
        if (request.getCookies() == null) {
//            PrintWriter writer = response.getWriter();
//            writer.append(JSON.toJSONString(notAuth));
            log.warn("cookies is null....");
            response.sendRedirect("/auth/401");
            return false;
        }
        if (request.getCookies().length <= 0) {
//            PrintWriter writer = response.getWriter();
//            writer.append(JSON.toJSONString(notAuth));
            log.warn("cookies.length <= 0....");
            response.sendRedirect("/auth/401");
            return false;
        }
        Optional<Cookie> optionalCookie = Arrays.stream(request.getCookies()).filter(e -> "JSESSIONID".equals(e.getName())).findAny();
        String cookieVal = optionalCookie.map(Cookie::getValue).orElse(null);
        Map<String, String> authMap = new HashMap<>(2);
        authMap.put("token", token);
        authMap.put("JSESSIONID", cookieVal);
        log.info("auth info: {}", authMap);
        if (UserAuthUtils.authToken(authMap)) {
            return true;
        }
//        PrintWriter writer = response.getWriter();
//        writer.append(JSON.toJSONString(notAuth));
        response.sendRedirect("/auth/401");
        return false;
    }

}
