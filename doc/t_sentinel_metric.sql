/*
Navicat MySQL Data Transfer

Source Server         : 10.112.1.20测试
Source Server Version : 50732
Source Host           : 10.112.1.20:3306
Source Database       : sentinel_pro

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-11-19 10:50:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_sentinel_metric
-- ----------------------------
DROP TABLE IF EXISTS `t_sentinel_metric`;
CREATE TABLE `t_sentinel_metric` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id，主键',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `app` varchar(100) DEFAULT NULL COMMENT '应用名称',
  `timestamp` datetime DEFAULT NULL COMMENT '统计时间',
  `resource` varchar(500) DEFAULT NULL COMMENT '资源名称',
  `pass_qps` int(11) DEFAULT NULL COMMENT '通过qps',
  `success_qps` int(11) DEFAULT NULL COMMENT '成功qps',
  `block_qps` int(11) DEFAULT NULL COMMENT '限流qps',
  `exception_qps` int(11) DEFAULT NULL COMMENT '发送异常的次数',
  `rt` double DEFAULT NULL COMMENT '所有successQps的rt的和',
  `_count` int(11) DEFAULT NULL COMMENT '本次聚合的总条数',
  `resource_code` int(11) DEFAULT NULL COMMENT '资源的hashCode',
  PRIMARY KEY (`id`),
  KEY `app_idx` (`app`) USING BTREE,
  KEY `resource_idx` (`resource`) USING BTREE,
  KEY `timestamp_idx` (`timestamp`) USING BTREE,
  KEY `gmt_create_idx` (`gmt_create`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=751013 DEFAULT CHARSET=utf8;
