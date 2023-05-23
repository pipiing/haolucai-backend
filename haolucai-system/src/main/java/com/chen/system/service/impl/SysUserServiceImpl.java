package com.chen.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.model.dto.system.LoginUserDTO;
import com.chen.model.entity.system.SysUser;
import com.chen.service.exception.ServiceException;
import com.chen.service.exception.enums.GlobalErrorCodeConstants;
import com.chen.system.service.SysUserService;
import com.chen.system.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
* @author chen
* @description 针对表【sys_user(用户信息表)】的数据库操作Service实现
* @createDate 2023-05-22 14:28:07
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    @Override
    public SysUser getSysUserByUserId(Long userId) {
        return this.getById(userId);
    }

    @Override
    public SysUser getSysUserByUserDTO(LoginUserDTO loginUserDTO) {
        String userName = loginUserDTO.getUserName();
        String password = loginUserDTO.getPassword();

        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StrUtil.isNotBlank(userName),SysUser::getUserName,userName);
        lambdaQueryWrapper.eq(StrUtil.isNotBlank(password),SysUser::getPassword,password);

        SysUser loginUser = this.getOne(lambdaQueryWrapper);

        if (loginUser == null){
            throw new ServiceException(GlobalErrorCodeConstants.ERROR.getCode(),"登陆账户或密码错误");
        }

        return loginUser;
    }

}




