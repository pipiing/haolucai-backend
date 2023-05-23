package com.chen.model.vo.system;

import lombok.Data;

/**
 * @author Pipiing
 * @date 2023/5/22 16:57
 */
@Data
public class SysUserVo {

    private Long userId;

    private String userName;

    private String nickName;

    private String userType;

    private String avatarUrl;

    private String token;
}
