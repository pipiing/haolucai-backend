package com.chen.model.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

/**
 * 角色菜单
 * @TableName sys_role_menu
 */
@Data
@Tag(name="SysRoleMenu",description = "角色和菜单关联")
@TableName(value ="sys_role_menu")
public class SysRoleMenu {

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    @TableId(type = IdType.INPUT)
    @TableField(value = "role_id")
    private Long roleId;

    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID")
    @TableField(value = "menu_id")
    private Long menuId;

}