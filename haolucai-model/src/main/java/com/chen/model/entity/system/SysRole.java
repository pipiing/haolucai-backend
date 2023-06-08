package com.chen.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chen.common.constant.UserConstants;
import com.chen.model.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@ApiModel(description = "角色")
@TableName(value = "sys_role")
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity {

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    @ApiModelProperty("角色名称")
    @TableField(value = "role_name")
    private String roleName;

    /**
     * 角色权限字符串
     */
    @NotBlank(message = "角色权限字符串不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    @ApiModelProperty("角色权限字符串")
    @TableField(value = "role_key")
    private String roleKey;

    /**
     * 显示顺序
     */
    @ApiModelProperty("角色权限字符串")
    @NotNull(message = "显示顺序不能为空")
    @TableField(value = "role_sort")
    private Integer roleSort;

    /**
     * 角色状态（0停用 1正常）
     */
    @ApiModelProperty("角色状态（0停用 1正常）")
    @TableField(value = "status")
    private String status;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    @TableField(value = "description")
    private String description;

    /**
     * 菜单ID组
     */
    @ApiModelProperty("菜单组")
    @TableField(exist = false)
    private Long[] menuIds;

    /**
     * 角色菜单权限列表
     */
    @ApiModelProperty("角色菜单权限列表")
    @TableField(exist = false)
    private Set<String> permissions;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public boolean isAdmin() {
        return UserConstants.ADMIN_ID.equals(this.getId());
    }
}