package com.chen.model.dto.system;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "RoleDTO", description = "角色DTO对象")
public class SysRoleDTO implements Serializable {

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long roleId;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 角色权限
     */
    @Schema(description = "角色权限")
    private String roleKey;

}
