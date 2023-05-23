package com.chen.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.model.entity.system.SysUser;
import com.chen.system.mapper.SysUserMapper;
import com.chen.system.service.ISysUserService;
import org.springframework.stereotype.Service;

/**
* @author chen
* @description 针对表【sys_user(用户信息表)】的数据库操作Service实现
* @createDate 2023-05-22 14:28:07
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements ISysUserService {

    @Override
    public SysUser getSysUserByUserId(Long userId) {
        return this.getById(userId);
    }

}




