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

 Date: 28/04/2021 23:47:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_distributed_lock
-- ----------------------------
DROP TABLE IF EXISTS `t_distributed_lock`;
CREATE TABLE `t_distributed_lock`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资源名称 唯一',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '资源描述',
  `version` int(255) NULL DEFAULT 0,
  `lock_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'LOCK: 锁已占用, UNLOCK: 锁未被占用',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique_resourceName`(`resource`) USING BTREE COMMENT 'resourceName资源名称唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_distributed_lock
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
