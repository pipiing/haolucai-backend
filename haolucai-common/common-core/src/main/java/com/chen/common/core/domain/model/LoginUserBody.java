package com.chen.common.core.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 用户登陆对象
 *
 * @author Pipiing
 * @date 2023/5/22 16:53
 */
@Data
@NoArgsConstructor
@Tag(name = "LoginUserBody", description = "用户登陆对象")
public class LoginUserBody {

    @NotNull(message = "登陆账号不能为空")
    @Size(min = 0, max = 20, message = "登录账号长度不能超过{max}个字符")
    @Schema(description = "登陆账号")
    private String userName;

    @NotNull(message = "登陆密码不能为空")
    @Size(min = 0, max = 30, message = "登录密码长度不能超过{max}个字符")
    @Schema(description = "登陆密码")
    private String password;

}
