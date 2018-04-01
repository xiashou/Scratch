DROP TABLE IF EXISTS `s_admin`;
CREATE TABLE `s_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
	`userName` varchar(20) DEFAULT NULL COMMENT '用户名',
  `password` varchar(20) DEFAULT NULL COMMENT '密码',
  `roleId` int(11) DEFAULT NULL COMMENT '角色Id',
  `locked` int(2) DEFAULT NULL COMMENT '是否锁定',
	`createdTime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `lastLogin` varchar(20) DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

DROP TABLE IF EXISTS `b_member`;
CREATE TABLE `b_member` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
	`unionId` varchar(50) DEFAULT NULL COMMENT 'unionId',
	`appid` varchar(50) DEFAULT NULL COMMENT 'appid',
  `openId` varchar(50) NOT NULL COMMENT 'openId',
  `nickName` varchar(100) DEFAULT NULL COMMENT '昵称',
  `gender` varchar(2) DEFAULT NULL COMMENT '性别',
  `language` varchar(10) DEFAULT NULL COMMENT '语言',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `country` varchar(50) DEFAULT NULL COMMENT '国家',
  `avatarUrl` text DEFAULT NULL COMMENT '头像',
  `timestamp` varchar(50) DEFAULT NULL COMMENT '时间戳',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

DROP TABLE IF EXISTS `b_minipro`;
CREATE TABLE `b_minipro` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
	`appid` varchar(50) NOT NULL COMMENT 'appid',
  `appName` varchar(50) NULL COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小程序名称表';

DROP TABLE IF EXISTS `b_banners`;
CREATE TABLE `b_banners` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
	`appid` varchar(50) NOT NULL COMMENT 'appid',
  `bannerUrl` varchar(50) NULL COMMENT '图片',
	`linkUrl` varchar(50) NULL COMMENT '链接地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Banner表';