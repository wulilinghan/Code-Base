package top.b0x0.demo.distributedLock.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 聚合api响应数据格式
 *
 * @author musui
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class R implements Serializable {
    private static final long serialVersionUID = -8163706238958883262L;

    private String message;
    private Integer code;
    private Object data;
    private Long timestamp = System.currentTimeMillis();

    public static R ok() {
        return new R().setCode(200);
    }

    public static R ok(Object data) {
        return new R().setCode(200).setMessage("").setData(data);
    }

    public static R fail(String message) {
        return new R().setCode(400).setMessage(message);
    }

    public static R fail(String message, Object data) {
        return new R().setCode(400).setMessage(message).setData(data);
    }

    public static R notAuth() {
        return new R().setCode(401).setMessage("未认证或认证信息异常");
    }
}
