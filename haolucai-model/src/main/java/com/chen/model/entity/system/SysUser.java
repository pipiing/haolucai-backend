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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用户信息表
 *
 * @TableName sys_user
 */
@Data
@NoArgsConstructor
@Tag(name = "SysUser", description = "用户")
@TableName(value = "sys_user")
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {

    /**
     * 用户账号
     */
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 20, message = "用户账号长度不能超过{max}个字符")
    @Schema(description = "用户账号")
    @TableField(value = "user_name")
    private String userName;

    /**
     * 用户密码
     */
    @NotBlank(message = "账户密码不能为空")
    @Size(min = 0, max = 30, message = "用户密码长度不能超过{max}个字符")
    @Schema(description = "用户密码")
    @TableField("password")
    private String password;

    /**
     * 用户昵称
     */
    @Size(min = 0, max = 20, message = "用户昵称长度不能超过{max}个字符")
    @Schema(description = "用户昵称")
    @TableField("nick_name")
    private String nickName;

    /**
     * 用户性别(0:男,1:女,2:未知)
     */
    @Schema(description = "性别")
    @TableField(value = "sex")
    private String sex;

    /**
     * 用户类型(0:管理员,1:商家,2:顾客)
     */
    @Schema(description = "用户类型(0:管理员,1:商家,2:顾客)")
    @TableField(value = "user_type")
    private String userType;

    /**
     * 手机号码
     */
    @Pattern(regexp = "0?(13|14|15|18)[0-9]{9}", message = "手机号码不合法")
    @Schema(description = "手机号码")
    @TableField("phone")
    private String phone;

    /**
     * 头像地址
     */
    @Schema(description = "头像地址")
    @TableField(value = "avatar_url")
    private String avatarUrl;

    /**
     * 微信openId
     */
    @Schema(description = "微信openId")
    @TableField(value = "open_id")
    private String openId;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @TableField(value = "description")
    private String description;

    /**
     * 状态(0:停用,1:正常)
     */
    @Schema(description = "状态(0:停用,1:正常)")
    @TableField(value = "status")
    private Integer status;

    /**
     * 角色对象列表
     */
    @Schema(description = "角色对象列表")
    @TableField(exist = false)
    private List<SysRole> roles;

    /**
     * 角色ID组
     */
    @Schema(description = "角色ID组")
    @TableField(exist = false)
    private Long[] roleIds;

    /**
     * 数据权限 当前角色ID
     */
    @Schema(description = "当前角色ID")
    @TableField(exist = false)
    private Long roleId;

    /**
     * 逻辑删除
     */
    @TableLogic
    @Schema(description = "逻辑删除")
    @TableField("is_deleted")
    private Integer isDeleted;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public SysUser(Long userId) {
        this.setId(userId);
    }

    public boolean isAdmin() {
        return SystemConstants.ADMIN_ID.equals(this.getId());
    }


}