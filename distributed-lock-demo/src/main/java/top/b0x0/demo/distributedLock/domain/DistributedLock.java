package top.b0x0.demo.distributedLock.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


/*
 * <pre>
 * CREATE TABLE `t_sys_distributedlock` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
 *   `resource_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资源名称 唯一',
 *   `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资源描述',
 *   `version` int(255) DEFAULT '0',
 *   `lock_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'LOCK: 锁已占用, UNLOCK: 锁未被占用',
 *   `expiration_time` datetime DEFAULT NULL COMMENT '过期时间',
 *   PRIMARY KEY (`id`) USING BTREE,
 *   UNIQUE KEY `idx_unique_resourceName` (`resource_name`) COMMENT 'resourceName资源名称唯一'
 * ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 * </pre>
 *
 * </pre>
 * INSERT INTO `mybatis-demo`.`t_sys_distributedlock`(`id`, `resource_name`, `description`, `version`, `lock_status`, `expiration_time`) VALUES (1, 'task-order', NULL, 0, 'UNLOCK', '2021-04-27 23:15:35');
 * </pre>
 */

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