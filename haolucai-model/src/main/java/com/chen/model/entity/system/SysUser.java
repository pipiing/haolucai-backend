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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用户信息表
 * @TableName sys_user
 */
@Data
@ApiModel(description = "用户")
@TableName(value ="sys_user")
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    @TableField(value = "user_name")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 20, message = "用户账号长度不能超过{max}个字符")
    private String userName;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    @TableField("password")
    @NotBlank(message = "账户密码不能为空")
    @Size(min = 0, max = 30, message = "用户密码长度不能超过{max}个字符")
    private String password;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    @TableField("nick_name")
    @Size(min = 0, max = 20, message = "用户昵称长度不能超过{max}个字符")
    private String nickName;

    /**
     * 用户性别(0:男,1:女,2:未知)
     */
    @ApiModelProperty(value = "性别")
    @TableField(value = "sex")
    private String sex;

    /**
     * 用户类型(0:管理员,1:商家,2:顾客)
     */
    @ApiModelProperty(value = "用户类型(0:管理员,1:商家,2:顾客)")
    @TableField(value = "user_type")
    private String userType;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    @TableField("phone")
    @Pattern(regexp = "0?(13|14|15|18)[0-9]{9}", message = "手机号码不合法")
    private String phone;

    /**
     * 头像地址
     */
    @ApiModelProperty(value = "头像地址")
    @TableField(value = "avatar_url")
    private String avatarUrl;

    /**
     * 微信openId
     */
    @ApiModelProperty("微信openId")
    @TableField(value = "open_id")
    private String openId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @TableField(value = "description")
    private String description;

    /**
     * 状态(0:停用,1:正常)
     */
    @ApiModelProperty("状态(0:停用,1:正常)")
    @TableField(value = "status")
    private Integer status;

    /**
     * 角色对象列表
     */
    @TableField(exist = false)
    @ApiModelProperty("角色对象列表")
    private List<SysRole> roles;

    /**
     * 角色ID组
     */
    @TableField(exist = false)
    @ApiModelProperty("角色ID组")
    private Long[] roleIds;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public boolean isAdmin() {
        return UserConstants.ADMIN_ID.equals(this.getId());
    }


}