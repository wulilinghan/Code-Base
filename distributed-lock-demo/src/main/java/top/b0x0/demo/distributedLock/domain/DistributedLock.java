package top.b0x0.demo.distributedLock.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author musui
 * @since 2021/04/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sys_distributedlock")
public class DistributedLock implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;

    private String resourceName;
    private String description;
    private String lockStatus;
    private int version;
    private LocalDateTime expirationTime;

//    private String createBy;
//    private LocalDateTime createTime;
//    private String updateBy;
//    private LocalDateTime updateTime;

}