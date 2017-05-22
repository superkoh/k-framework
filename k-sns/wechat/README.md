# 初始化SQL

    DROP TABLE IF EXISTS `sns_wx_user`;
    
    CREATE TABLE `sns_wx_user` (
      `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
      `open_id_for_mp` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT '' COMMENT '公众号openid',
      `open_id_for_open` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT '' COMMENT '开放平台openid',
      `open_id_for_app` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT '' COMMENT '小程序openid',
      `union_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '微信统一ID',
      `nickname` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '用户微信昵称',
      `gender` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'UNKNOWN' COMMENT '用户性别',
      `country` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '国家',
      `province` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '省',
      `city` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '市',
      `avatar_url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '头像',
      `create_time` datetime NOT NULL,
      `update_time` datetime NOT NULL,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
    
    DROP TABLE IF EXISTS `sns_wx_app_session`;
    
    CREATE TABLE `sns_wx_app_session` (
      `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
      `open_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'openid',
      `session_key` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'sessionKey',
      `expires_in` int(11) NOT NULL COMMENT '过期时间',
      PRIMARY KEY (`id`),
      KEY `open_id` (`open_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;