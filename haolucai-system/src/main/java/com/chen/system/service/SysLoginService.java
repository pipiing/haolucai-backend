package com.chen.system.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.common.enums.UserStatus;
import com.chen.model.entity.system.SysUser;
import com.chen.service.exception.ServiceException;
import com.chen.service.exception.enums.GlobalErrorCodeConstants;
import com.chen.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统登录校验服务
 *
 * @author Pipiing
 * @date 2023/05/23 20:45:23
 */
@Slf4j
@Service
public class SysLoginService {

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 登录验证
     *
     * @param userName 用户名
     * @param password 密码
     * @return token令牌
     */
    public String login(String userName, String password) {
        // 通过用户名和密码，加载用户
        SysUser loginUser = loadUserByUsername(userName,password);
        log.info("用户登录成功,用户ID:{}",loginUser.getId());
        // 根据用户ID，进行登陆（Sa-Token）
        StpUtil.login(loginUser.getId());
        // 生成token，并返回
        return StpUtil.getTokenInfo().getTokenValue();
    }


    /**
     * 通过用户名和密码，加载用户
     *
     * @param userName 用户名
     * @param password 密码
     * @return {@link SysUser }
     */
    private SysUser loadUserByUsername(String userName ,String password) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StrUtil.isNotBlank(userName),SysUser::getUserName,userName);
        lambdaQueryWrapper.eq(StrUtil.isNotBlank(password),SysUser::getPassword,password);

        SysUser loginUser = sysUserMapper.selectOne(lambdaQueryWrapper);

        if (ObjectUtil.isNull(loginUser)) {
            log.info("登录用户：{} 用户名或密码错误.", userName);
            throw new ServiceException(GlobalErrorCodeConstants.ERROR.getCode(),"用户名或密码错误");
        } else if (UserStatus.DISABLE.getCode().equals(loginUser.getStatus())) {
            log.info("登录用户：{} 已被停用.", userName);
            throw new ServiceException(GlobalErrorCodeConstants.ERROR.getCode(), "登录用户被停用");
        }

        return loginUser;
    }
}
