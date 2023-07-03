package com.chen.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chen.common.constant.SystemConstants;
import com.chen.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 角色表
 *
 * @TableName sys_role
 */
@Data
@Tag(name = "SysRole", description = "角色")
@TableName(value = "sys_role")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity {

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    @Schema(description = "角色名称")
    @TableField(value = "role_name")
    private String roleName;

    /**
     * 角色权限字符串
     */
    @NotBlank(message = "角色权限字符串不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    @Schema(description = "角色权限字符串")
    @TableField(value = "role_key")
    private String roleKey;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    @Schema(description = "显示顺序")
    @TableField(value = "role_sort")
    private Integer roleSort;

    /**
     * 角色状态（0停用 1正常）
     */
    @Schema(description = "角色状态（0停用 1正常）")
    @TableField(value = "status")
    private String status;

    /**
     * 描述
     */
    @Schema(description = "描述")
    @TableField(value = "description")
    private String description;

    /**
     * 菜单ID组
     */
    @Schema(description = "菜单组")
    @TableField(exist = false)
    private Long[] menuIds;

    /**
     * 角色菜单权限列表
     */
    @Schema(description = "角色菜单权限列表")
    @TableField(exist = false)
    private Set<String> permissions;

    /**
     * 用户是否存在此角色标识 默认不存在
     */
    @Schema(description = "用户是否存在此角色标识 默认不存在")
    @TableField(exist = false)
    private boolean flag = false;

    /**
     * 逻辑删除
     */
    @TableLogic
    @Schema(description = "逻辑删除")
    @TableField("is_deleted")
    private Integer isDeleted;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public SysRole(Long roleId) {
        this.setId(roleId);
    }

    public boolean isAdmin() {
        return SystemConstants.ADMIN_ID.equals(this.getId());
    }
}