CREATE DATABASE IF NOT EXISTS `hlc-react` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_bin';

use `hlc-react`;

# 创建用户信息表
CREATE TABLE IF NOT EXISTS `sys_user`
(
    `id`          bigint(20)  NOT NULL COMMENT '用户Id',
    `user_name`   varchar(20) NOT NULL DEFAULT '' COMMENT '用户账号',
    `password`    varchar(30) NOT NULL DEFAULT '' COMMENT '用户密码',
    `nick_name`   varchar(30)          DEFAULT NULL COMMENT '用户昵称',
    `sex`         char(1)              DEFAULT NULL COMMENT '用户性别(0:男,1:女,2:未知)',
    `user_type`   char(1)              DEFAULT NULL COMMENT '用户类型(0:管理员,1:商家,2:顾客)',
    `phone`       varchar(11)          DEFAULT NULL COMMENT '手机号码',
    `avatar_url`  varchar(255)         DEFAULT NULL COMMENT '头像地址',
    `open_id`     varchar(255)         DEFAULT NULL COMMENT '微信openId',
    `description` varchar(255)         DEFAULT NULL COMMENT '备注',
    `status`      tinyint(3)           DEFAULT NULL COMMENT '状态(0:停用,1:正常)',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  tinyint(3)  NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`user_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户信息表';