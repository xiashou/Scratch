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
	`sortNo` int(2) NULL COMMENT '排序',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Banner表';

DROP TABLE IF EXISTS `b_store`;
CREATE TABLE `b_store` (
  	`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
	`appid` varchar(50) DEFAULT NULL COMMENT 'appid',
  	`name` varchar(100) NOT NULL COMMENT '门店名称',
  	`address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  	`phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  	`introduction` varchar(1000) DEFAULT NULL COMMENT '简介',
  	`headUrl` varchar(200) DEFAULT NULL COMMENT '门头照片',
  	`imageUrl` varchar(200) DEFAULT NULL COMMENT '大图照片',
  	`locationx` varchar(20) DEFAULT NULL COMMENT '国家',
	`locationy` varchar(20) DEFAULT NULL COMMENT '头像',
  	`enable` int(1) DEFAULT NULL COMMENT '是否可用',
  	`createdTime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门店表';

DROP TABLE IF EXISTS `b_activity`;
CREATE TABLE `b_activity` (
  	`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
	`appid` varchar(50) DEFAULT NULL COMMENT 'appid',
  	`name` varchar(100) NOT NULL COMMENT '产品名称',
  	`price` float unsigned DEFAULT '0' COMMENT '价格',
  	`virNumber` int(11) unsigned DEFAULT 0 COMMENT '虚拟参与人数',
  	`actNumber` int(11) unsigned DEFAULT 0 COMMENT '实际参与人数',
  	`broNumber` int(11) unsigned DEFAULT 0 COMMENT '浏览人数',
  	`imageUrl` varchar(200) DEFAULT NULL COMMENT '图片',
 	 `enable` int(1) DEFAULT NULL COMMENT '是否可用',
  	`createdTime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表';


DROP TABLE IF EXISTS `b_payment`;
CREATE TABLE `b_payment` (
  	`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
	`outTradeNo` varchar(50) DEFAULT NULL COMMENT '订单号',
  	`transactionId` varchar(50) DEFAULT NULL COMMENT '微信订单号',
  	`appid` varchar(50) DEFAULT NULL COMMENT '小程序id',
  	`mchId` varchar(50) DEFAULT NULL COMMENT '商户id',
  	`openid` varchar(200) DEFAULT NULL COMMENT '会员openId',
  	`tradeType` varchar(20) DEFAULT NULL COMMENT '交易类型',
  	`bankType` varchar(20) DEFAULT NULL COMMENT '付款银行',
  	`productName` varchar(50) DEFAULT NULL COMMENT '商品名称',
  	`price` int(11) unsigned DEFAULT NULL COMMENT '订单金额',
  	`totalFee` int(11) unsigned DEFAULT NULL COMMENT '订单金额',
  	`attach` int(5) unsigned DEFAULT NULL COMMENT '数量',
	`feeType` varchar(20) DEFAULT NULL COMMENT '货币种类',
	`isSubscribe` varchar(4) DEFAULT NULL COMMENT '是否关注公众号',
	`clientIp` varchar(20) DEFAULT NULL COMMENT '客户IP',
  	`timeEnd` varchar(20) DEFAULT NULL COMMENT '支付完成时间',
  	`resultCode` varchar(20) DEFAULT NULL COMMENT '支付结果',
  	`errorCode` varchar(40) DEFAULT NULL COMMENT '错误代码',
  	`errorMsg` varchar(200) DEFAULT NULL COMMENT '错误描述',
  	`createdTime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  	`notifyTime` varchar(20) DEFAULT NULL COMMENT '回调时间',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='付款信息表';


DROP TABLE IF EXISTS `b_coupon`;
CREATE TABLE `b_coupon` (
  	`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
	`storeId` int(11) unsigned NOT NULL COMMENT '门店id',
  	`name` varchar(100) DEFAULT NULL COMMENT '优惠券名称',
	`describ` varchar(100) DEFAULT NULL COMMENT '类型说明',
	`number` int(11) unsigned DEFAULT NULL COMMENT '数量',
	`imageUrl` varchar(200) DEFAULT NULL COMMENT '图片',
	`endDate` varchar(20) DEFAULT NULL COMMENT '有效期至',
	`createdTime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家优惠券表';


DROP TABLE IF EXISTS `b_actcoupon`;
CREATE TABLE `b_actcoupon` (
  	`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
	`actId` int(11) unsigned NOT NULL COMMENT '活动id',
	`couponId` int(11) unsigned NOT NULL COMMENT '优惠券id',
	`number` int(11) unsigned DEFAULT 0 COMMENT '剩余数量',
	`probability` int(5) unsigned DEFAULT 0 COMMENT '产生概率',
	`endDate` varchar(20) DEFAULT NULL COMMENT '有效期至',
	`createdTime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动优惠券表';


DROP TABLE IF EXISTS `b_memcoupon`;
CREATE TABLE `b_memcoupon` (
  	`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
	`openid` varchar(200) DEFAULT NULL COMMENT '会员openId',
	`actcouponId` int(11) unsigned NOT NULL COMMENT '活动优惠券id',
	`code` varchar(20) NOT NULL COMMENT '优惠券码',
	`endDate` varchar(20) DEFAULT NULL COMMENT '有效期至',
	`status` int(1) unsigned DEFAULT 0 COMMENT '状态',
	`createdTime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动优惠券表';


DROP TABLE IF EXISTS `b_memscratch`;
CREATE TABLE `b_memscratch` (
  	`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
	`openid` varchar(200) DEFAULT NULL COMMENT '会员openId',
	`actId` int(11) unsigned NOT NULL COMMENT '活动id',
	`name` varchar(20) NOT NULL COMMENT '奖品名称',
	`price` decimal(10, 2) unsigned DEFAULT 0 COMMENT '刮中金额',
	`tradeNo` varchar(30) DEFAULT NULL COMMENT '订单号',
	`isscratch` int(1) unsigned DEFAULT 0 COMMENT '是否刮奖',
	`scratchTime` varchar(20) DEFAULT NULL COMMENT '刮奖时间',
	`status` int(1) unsigned DEFAULT 0 COMMENT '状态',
	`createdTime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动刮奖信息表';

DROP TABLE IF EXISTS `b_scratch`;
CREATE TABLE `b_scratch` (
  	`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
	`actId` int(11) unsigned NOT NULL COMMENT '活动id',
	`name` varchar(50) NOT NULL COMMENT '奖品名称',
	`price` decimal(10, 2) unsigned DEFAULT 0 COMMENT '金额',
	`probability` int(5) unsigned DEFAULT 0 COMMENT '中奖概率',
	`createdTime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='刮奖设置表';

DROP TABLE IF EXISTS `b_scratch_text`;
CREATE TABLE `b_scratch_text` (
  	`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
	`actId` int(11) unsigned NOT NULL COMMENT '活动id',
	`name` varchar(50) NOT NULL COMMENT '未中奖文本',
	`describ` text DEFAULT NULL COMMENT '说明',
	`createdTime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='刮奖文本设置表';

DROP TABLE IF EXISTS `b_entpayment`;
CREATE TABLE `b_entpayment` (
  	`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  	`appid` varchar(50) DEFAULT NULL COMMENT '小程序id',
  	`mchId` varchar(50) DEFAULT NULL COMMENT '商户id',
  	`resultCode` varchar(20) DEFAULT NULL COMMENT '支付结果',
  	`errorCode` varchar(40) DEFAULT NULL COMMENT '错误代码',
  	`errorMsg` varchar(200) DEFAULT NULL COMMENT '错误描述',
  	`tradeNo` varchar(50) DEFAULT NULL COMMENT '订单号',
  	`paymentNo` varchar(50) DEFAULT NULL COMMENT '微信订单号',
  	`paymentTime` varchar(20) DEFAULT NULL COMMENT '支付完成时间',
  	`createdTime` varchar(20) DEFAULT NULL COMMENT '创建时间',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业付款信息表';

