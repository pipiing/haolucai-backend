CREATE DATABASE IF NOT EXISTS `hlc-react` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_bin';

use `hlc-react`;

# 创建用户信息表
CREATE TABLE IF NOT EXISTS `sys_user`
(
    `id`          bigint(20)   NOT NULL COMMENT '用户ID',
    `user_name`   varchar(20)  NOT NULL DEFAULT '' COMMENT '用户账号',
    `password`    varchar(100) NOT NULL DEFAULT '' COMMENT '用户密码',
    `nick_name`   varchar(30)           DEFAULT NULL COMMENT '用户昵称',
    `sex`         char(1)               DEFAULT NULL COMMENT '用户性别(0:男,1:女,2:未知)',
    `user_type`   char(1)               DEFAULT NULL COMMENT '用户类型(0:管理员,1:商家,2:顾客)',
    `phone`       varchar(11)           DEFAULT NULL COMMENT '手机号码',
    `avatar_url`  varchar(255)          DEFAULT NULL COMMENT '头像地址',
    `open_id`     varchar(255)          DEFAULT NULL COMMENT '微信openId',
    `description` varchar(255)          DEFAULT NULL COMMENT '备注',
    `status`      tinyint(3)            DEFAULT NULL COMMENT '状态(0:停用,1:正常)',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  tinyint(3)   NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`user_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户信息表';

# 创建角色表
CREATE TABLE IF NOT EXISTS `sys_role`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name`   varchar(30) NOT NULL DEFAULT '' COMMENT '角色名称',
    `role_key`    varchar(20)          DEFAULT NULL COMMENT '角色权限字符串',
    `role_sort`   int(4)      NOT NULL COMMENT '显示顺序',
    `status`      char(1)     NOT NULL COMMENT '角色状态（0停用 1正常）',
    `description` varchar(255)         DEFAULT NULL COMMENT '描述',
    `create_time` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  tinyint(3)  NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8 COMMENT ='角色表';

# 创建用户角色关系表
CREATE TABLE IF NOT EXISTS `sys_user_role`
(
    `role_id`     bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
    `user_id`     bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
    KEY `idx_role_id` (`role_id`),
    KEY `idx_admin_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8 COMMENT ='用户角色关系表';

# 创建菜单表
CREATE TABLE `sys_menu`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `parent_id`   bigint(20)  NOT NULL DEFAULT '0' COMMENT '父菜单ID',
    `name`        varchar(20) NOT NULL DEFAULT '' COMMENT '菜单名称',
    `type`        char(3)     NOT NULL DEFAULT '0' COMMENT '类型(D:目录,M:菜单,B:按钮)',
    `path`        varchar(100)         DEFAULT NULL COMMENT '路由地址',
    `component`   varchar(100)         DEFAULT NULL COMMENT '组件路径',
    `perms`       varchar(100)         DEFAULT NULL COMMENT '权限标识',
    `icon`        varchar(100)         DEFAULT NULL COMMENT '图标',
    `menu_sort`   int(11)              DEFAULT NULL COMMENT '显示顺序',
    `status`      tinyint(4)           DEFAULT NULL COMMENT '状态(0:禁止,1:正常)',
    `create_time` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  tinyint(3)  NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 58
  DEFAULT CHARSET = utf8mb4 COMMENT ='菜单表';

# 创建角色菜单权限关系表
CREATE TABLE `sys_role_menu`
(
    `role_id`     bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
    `menu_id`     bigint(11) NOT NULL DEFAULT '0' COMMENT '菜单ID',
    KEY `idx_role_id` (`role_id`),
    KEY `idx_menu_id` (`menu_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 33
  DEFAULT CHARSET = utf8 COMMENT ='角色菜单关系表';