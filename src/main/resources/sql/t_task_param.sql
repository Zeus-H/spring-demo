/*
 Navicat Premium Data Transfer

 Source Server         : 测试环境RDS
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : rm-wz90kr468ltid4u17.mysql.rds.aliyuncs.com:3306
 Source Schema         : trade

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 02/09/2022 09:27:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_task_param
-- ----------------------------
DROP TABLE IF EXISTS `t_task_param`;
CREATE TABLE `t_task_param`  (
  `ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `TASK_CODE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务编码',
  `PARAM_NAME` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数名称',
  `PARAM_VALUE` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数值',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数描述'
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务参数' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
