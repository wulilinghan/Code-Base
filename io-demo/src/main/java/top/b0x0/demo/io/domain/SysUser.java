package top.b0x0.demo.io.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统用户(SysUser)实体类
 *
 * @author TANG
 * @since 2020-12-17 09:51:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class SysUser implements Serializable {
    private static final long serialVersionUID = 649715419116774602L;

    @ApiModelProperty(value = "ID", hidden = true)
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户性别")
    private String gender;

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "头像真实名称", hidden = true)
    private String avatarName;

    @ApiModelProperty(value = "头像真实路径", hidden = true)
    private String avatarPath;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "是否为admin账号", hidden = true)
    private Boolean isAdmin;

    @ApiModelProperty(value = "是否启用 1启用、0禁用")
    private Integer enabled;

    private String salt;

    private String createBy;

    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime pwdResetTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}