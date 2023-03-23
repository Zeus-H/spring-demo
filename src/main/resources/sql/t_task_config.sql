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

 Date: 31/08/2022 16:03:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_task_config
-- ----------------------------
DROP TABLE IF EXISTS `t_task_config`;
CREATE TABLE `t_task_config`  (
  `ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `TASK_CODE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务编码(重启JVM生效)',
  `TASK_NAME` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `TASK_HANDLER_BEAN` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务处理器(spring bean name)(异步状态下才允许一个处理器分配给多个任务编码)(重启JVM生效)',
  `TASK_DESCRIPTION` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `RUN_RULE` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行规则连续执行状态下会跳过当前执行指令',
  `IS_ENABLED` int(0) NOT NULL DEFAULT 1 COMMENT '是否启用 ',
  `IS_CONTINUOUS` int(0) NULL DEFAULT 0 COMMENT '这是自爆程序,除非你清楚它做了什么否则不要设置为1',
  `IS_LOGGING` int(0) NULL DEFAULT 1 COMMENT '是否记录日志',
  `IS_STATISTICS` int(0) NULL DEFAULT 0 COMMENT '是否统计状态 ',
  `REVISION` int(0) NULL DEFAULT 0 COMMENT '乐观锁',
  `CREATED_BY` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `UPDATED_BY` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
