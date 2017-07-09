/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : sensor_annotation

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2017-07-07 14:34:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for column_type
-- ----------------------------
DROP TABLE IF EXISTS `column_type`;
CREATE TABLE `column_type` (
  `table_id` varchar(45) NOT NULL,
  `company` text,
  `unit` text,
  `sensorType` text,
  `property` text,
  `region` text,
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for datatransform
-- ----------------------------
DROP TABLE IF EXISTS `datatransform`;
CREATE TABLE `datatransform` (
  `device_id` int(11) NOT NULL,
  `provider` varchar(45) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `property` varchar(45) DEFAULT NULL,
  `sensorType` varchar(45) DEFAULT NULL,
  `unit` varchar(45) DEFAULT NULL,
  `region` varchar(45) DEFAULT NULL,
  `spot` varchar(45) DEFAULT NULL,
  `company` varchar(45) DEFAULT NULL,
  `property_url` text,
  `company_url` text,
  `region_url` text,
  `unit_url` text,
  `sensorType_url` text,
  `status` int(11) DEFAULT '0',
  `table_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for historydata
-- ----------------------------
DROP TABLE IF EXISTS `historydata`;
CREATE TABLE `historydata` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deviceId` varchar(45) DEFAULT NULL,
  `samplingTime` timestamp NULL DEFAULT NULL,
  `value` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for owldata
-- ----------------------------
DROP TABLE IF EXISTS `owldata`;
CREATE TABLE `owldata` (
  `owl_id` int(11) NOT NULL AUTO_INCREMENT,
  `owl_name` varchar(255) NOT NULL,
  `owl_root` varchar(255) NOT NULL,
  `owl_description` varchar(255) DEFAULT NULL,
  `owl_rule` text,
  `owl_filename` varchar(255) DEFAULT NULL,
  `owl_uri` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`owl_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_information
-- ----------------------------
DROP TABLE IF EXISTS `user_information`;
CREATE TABLE `user_information` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

