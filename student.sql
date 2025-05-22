/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50744
Source Host           : localhost:3306
Source Database       : student

Target Server Type    : MYSQL
Target Server Version : 50744
File Encoding         : 65001

Date: 2024-08-05 23:31:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员信息表';

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', 'admin', 'admin', '管理员', 'ADMIN');

-- ----------------------------
-- Table structure for `course`
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '课程名称',
  `no` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '课程编号',
  `descr` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '课程描述',
  `times` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '课时',
  `teacher` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '任课老师',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程信息表';

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('1', '大学英语', 'CS001', '大学英语', '24', '张三');
INSERT INTO `course` VALUES ('2', '大学数学', 'CS002', '大学数学', '24', '李四');
INSERT INTO `course` VALUES ('3', '大学物理', 'CS003', '大学物理', '24', '王五');
INSERT INTO `course` VALUES ('4', '线性代数', 'CS004', '线性代数', '24', '马六');
INSERT INTO `course` VALUES ('5', '高等数学', 'CS005', '高等数学', '24', '王二');
INSERT INTO `course` VALUES ('6', '马克思主义', 'CS006', '马克思主义', '24', '思思');
INSERT INTO `course` VALUES ('7', '统计学', 'CS007', '统计学', '24', '柳少侠');

-- ----------------------------
-- Table structure for `grade`
-- ----------------------------
DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_id` int(10) DEFAULT NULL COMMENT '课程ID',
  `student_id` int(10) DEFAULT NULL COMMENT '学生ID',
  `score` double(10,0) DEFAULT NULL COMMENT '分数',
  `comment` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '评论',
  `feedback` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '反馈',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成绩表';

-- ----------------------------
-- Records of grade
-- ----------------------------
INSERT INTO `grade` VALUES ('1', '1', '6', '80', 'good boy', '不错哦111');

-- ----------------------------
-- Table structure for `student`
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `sex` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `birth` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '生日',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生信息表';

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('1', 'admin', 'admin', '管理员', null, null, null, null, null, 'STUDENT');
INSERT INTO `student` VALUES ('6', 's003', '123456', '王五', '12345', 'wwu@qq.com', '男', '2024-07-03', null, 'STUDENT');
INSERT INTO `student` VALUES ('8', 's005', '123', 's005', null, null, null, '2024-08-02', null, 'STUDENT');
INSERT INTO `student` VALUES ('10', 's007', '123', 's007', null, null, null, null, null, 'STUDENT');
INSERT INTO `student` VALUES ('12', 's002', '123456', '李四', '11111111', 'lisi@xm.com', '男', '2024-07-01', 'http://localhost:9090/files/download?fileName=17225132795961722511023084OIP-C.jpg', 'STUDENT');

-- ----------------------------
-- Table structure for `student_course`
-- ----------------------------
DROP TABLE IF EXISTS `student_course`;
CREATE TABLE `student_course` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '课程名称',
  `no` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '课程编号',
  `student_id` int(10) DEFAULT NULL COMMENT '学生id',
  `course_id` int(10) DEFAULT NULL COMMENT '课程id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生选课';

-- ----------------------------
-- Records of student_course
-- ----------------------------
INSERT INTO `student_course` VALUES ('4', '马克思主义', 'CS006', '12', '6');
INSERT INTO `student_course` VALUES ('5', '线性代数', 'CS004', '12', '4');
INSERT INTO `student_course` VALUES ('7', '大学英语', 'CS001', '6', '1');
