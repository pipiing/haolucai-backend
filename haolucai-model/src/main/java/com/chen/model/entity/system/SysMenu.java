package com.chen.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chen.model.entity.TreeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 菜单表
 * @TableName sys_menu
 */
@Data
@Tag(name="SysMenu",description = "菜单")
@EqualsAndHashCode(callSuper = true)
@TableName(value ="sys_menu")
public class SysMenu extends TreeEntity<SysMenu> {


    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 0, max = 50, message = "菜单名称长度不能超过{max}个字符")
    @Schema(description="菜单名称")
    @TableField(value = "name")
    private String name;

    /**
     * 类型(D:目录,M:菜单,B:按钮)
     */
    @NotBlank(message = "菜单类型不能为空")
    @Schema(description="类型(D:目录,M:菜单,B:按钮)")
    @TableField(value = "type")
    private String type;

    /**
     * 路由地址
     */
    @Size(min = 0, max = 200, message = "路由地址不能超过{max}个字符")
    @Schema(description="路由地址")
    @TableField(value = "path")
    private String path;

    /**
     * 组件路径
     */
    @Size(min = 0, max = 200, message = "组件路径不能超过{max}个字符")
    @Schema(description="组件路径")
    @TableField(value = "component")
    private String component;

    /**
     * 权限标识
     */
    @Size(min = 0, max = 100, message = "权限标识长度不能超过{max}个字符")
    @Schema(description="权限标识")
    @TableField(value = "perms")
    private String perms;

    /**
     * 图标
     */
    @Schema(description="图标")
    @TableField(value = "icon")
    private String icon;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    @Schema(description="显示顺序")
    @TableField(value = "menu_sort")
    private Integer menuSort;

    /**
     * 状态(0:禁止,1:正常)
     */
    @Schema(description="状态(0:禁止,1:正常)")
    @TableField(value = "status")
    private Integer status;

    /**
     * 逻辑删除
     */
    @TableLogic
    @Schema(description = "逻辑删除")
    @TableField("is_deleted")
    private Integer isDeleted;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}