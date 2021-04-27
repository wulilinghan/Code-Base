/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3306
 Source Schema         : mybatis-demo

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 27/04/2021 23:16:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_sys_distributedlock
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_distributedlock`;
CREATE TABLE `t_sys_distributedlock`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `resource_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资源名称 唯一',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '资源描述',
  `version` int(255) NULL DEFAULT 0,
  `lock_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'LOCK: 锁已占用, UNLOCK: 锁未被占用',
  `expiration_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique_resourceName`(`resource_name`) USING BTREE COMMENT 'resourceName资源名称唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_distributedlock
-- ----------------------------
INSERT INTO `t_sys_distributedlock` VALUES (1, 'task-order', NULL, 0, 'UNLOCK', '2021-04-27 23:15:35');

SET FOREIGN_KEY_CHECKS = 1;
