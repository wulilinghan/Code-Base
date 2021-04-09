package top.b0x0.demo.http.common;

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
    private static final long serialVersionUID = 2347014227849421617L;
    private String message;
    private Integer code;
    private Object data;

    public static R ok(Object data) {
        return new R().setCode(200).setMessage("").setData(data);
    }
}
