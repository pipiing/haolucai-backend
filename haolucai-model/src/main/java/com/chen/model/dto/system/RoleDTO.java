package com.chen.model.dto.system;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色DTO对象
 *
 * @author Pipiing
 * @description
 * @date 2023/06/05 20:47:56
 */
@Data
@NoArgsConstructor
public class RoleDTO implements Serializable {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限
     */
    private String roleKey;

}
