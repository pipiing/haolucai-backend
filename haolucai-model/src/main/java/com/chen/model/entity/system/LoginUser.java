package com.chen.model.entity.system;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * 登录用户身份权限
 * @author Pipiing
 * @date 2023/5/22 16:57
 */
@Data
@ToString
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户类型
     */
    private String userType;


    /**
     * 用户唯一标识（Token令牌）
     */
    private String token;

    /**
     * 菜单权限
     */
    private Set<String> menuPermission;

    /**
     * 角色权限
     */
    private Set<String> rolePermission;


    /**
     * 数据权限 当前角色ID
     */
    private Long roleId;

}
