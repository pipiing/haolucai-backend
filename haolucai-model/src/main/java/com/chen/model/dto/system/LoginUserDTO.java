package com.chen.model.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * 用户登陆DTO
 * @author Pipiing
 * @date 2023/5/22 16:53
 */
@Data
@ToString
@ApiModel(description = "用户登陆DTO")
public class LoginUserDTO {

    @NotNull(message = "登陆账号不能为空")
    @ApiModelProperty("登陆账号")
    private String userName;

    @NotNull(message = "登陆密码不能为空")
    @ApiModelProperty("登陆密码")
    private String password;

}
