package top.b0x0.demo.http.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 聚合api响应数据格式
 *
 * @author musui
 */
@Data
@Accessors(chain = true)
public class JuheResponse implements Serializable {
    private static final long serialVersionUID = 2347059227849421617L;
    private String reason;
    private String resultcode;
    private Object result;
    private Integer error_code;
}
