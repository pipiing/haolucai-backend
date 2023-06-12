package com.chen.model.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Pipiing
 * @date 2023/5/25 10:35
 */
@Data
@ApiModel(description = "用户和角色关联")
@TableName("sys_user_role")
public class SysUserRole {


    @ApiModelProperty(value = "用户ID")
    @TableId(type = IdType.INPUT)
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "角色ID")
    @TableField("role_id")
    private Long roleId;
}
