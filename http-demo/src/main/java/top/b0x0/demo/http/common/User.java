package top.b0x0.demo.http.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * @author TANG
 * @since 2021/04/10
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer uid;

    @NotBlank(message = "名称不能为空")
    private String username;

    @Range(min = 0, max = 120, message = "年龄范围在0~120之间")
    private Integer age;

    @NotBlank(message = "地址不能为空")
    private String address;
}
