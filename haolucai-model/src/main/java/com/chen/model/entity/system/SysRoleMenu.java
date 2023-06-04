package com.chen.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chen.model.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色菜单
 * @TableName sys_role_menu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "角色和菜单关联")
@TableName(value ="sys_role_menu")
public class SysRoleMenu extends BaseEntity {

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    @TableField(value = "role_id")
    private Long roleId;

    /**
     * 菜单ID
     */
    @ApiModelProperty(value = "菜单ID")
    @TableField(value = "menu_id")
    private Long menuId;


    private static final long serialVersionUID = 1L;
}