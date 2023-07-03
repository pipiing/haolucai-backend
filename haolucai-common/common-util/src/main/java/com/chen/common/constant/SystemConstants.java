package com.chen.common.constant;

/**
 * 系统模块常量信息
 *
 * @author Pipiing
 * @date 2023/05/26 11:31:04
 */
public interface SystemConstants {


    /**
     * 正常状态
     */
    String NORMAL = "0";

    /**
     * 异常状态
     */
    String EXCEPTION = "1";

    /**
     * 用户正常状态
     */
    String USER_NORMAL = "0";

    /**
     * 用户封禁状态
     */
    String USER_DISABLE = "1";

    /**
     * 角色正常状态
     */
    String ROLE_NORMAL = "0";

    /**
     * 角色封禁状态
     */
    String ROLE_DISABLE = "1";

    /**
     * 字典正常状态
     */
    String DICT_NORMAL = "0";

    /**
     * 是否为系统默认（是）
     */
    String YES = "Y";

    /**
     * 菜单正常状态
     */
    String MENU_NORMAL = "1";

    /**
     * 菜单停用状态
     */
    String MENU_DISABLE = "0";

    /**
     * 菜单类型（目录）
     */
    String TYPE_DIR = "D";

    /**
     * 菜单类型（菜单）
     */
    String TYPE_MENU = "M";

    /**
     * 菜单类型（按钮）
     */
    String TYPE_BUTTON = "B";

    /**
     * 唯一
     */
    String UNIQUE = "0";

    /**
     * 不唯一
     */
    String NOT_UNIQUE = "1";

    /**
     * 管理员ID
     */
    Long ADMIN_ID = 1L;

    /**
     * 性别未知
     */
    String SEX_UNKNOW = "2";
}
