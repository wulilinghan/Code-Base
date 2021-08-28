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
@TableName("t_distributed_lock")
public class DistributedLock implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;

    private String resource;
    private String description;
    private String lockStatus;
    private int version;
    private LocalDateTime createTime;

}