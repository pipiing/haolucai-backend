package com.chen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.model.entity.system.SysUser;

/**
* @author chen
* @description 针对表【sys_user(用户信息表)】的数据库操作Service
* @createDate 2023-05-22 14:28:07
*/
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return {@link SysUser } 用户信息
     */
    SysUser getSysUserByUserId(Long userId);
}
