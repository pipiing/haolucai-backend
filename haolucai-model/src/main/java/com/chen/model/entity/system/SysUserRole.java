package com.chen.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chen.model.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Pipiing
 * @date 2023/5/25 10:35
 */
@Data
@ApiModel(description = "用户角色关系")
@TableName("sys_user_role")
public class SysUserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色Id")
    @TableField("role_id")
    private Long roleId;

    @ApiModelProperty(value = "用户Id")
    @TableField("user_id")
    private Long userId;

}
