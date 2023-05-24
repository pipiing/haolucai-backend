package com.chen.system.service;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.common.constant.CacheConstants;
import com.chen.common.enums.DeviceType;
import com.chen.common.enums.UserStatus;
import com.chen.common.utils.RedisCache;
import com.chen.model.entity.system.LoginUser;
import com.chen.model.entity.system.SysUser;
import com.chen.service.exception.ServiceException;
import com.chen.service.exception.enums.GlobalErrorCodeConstants;
import com.chen.service.helper.LoginHelper;
import com.chen.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

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

    @Autowired
    private RedisCache redisCache;

    @Value("${user.password.maxRetryCount}")
    private Integer maxRetryCount;

    @Value("${user.password.lockTime}")
    private Integer lockTime;

    /**
     * 登录验证
     *
     * @param userName 用户名
     * @param password 密码
     * @return token令牌
     */
    public String login(String userName, String password) {
        // 通过用户名和密码，加载用户
        SysUser user = this.loadUserByUsername(userName);
        this.checkLogin(userName, () -> !BCrypt.checkpw(password, user.getPassword()));
        // 根据登录用户信息，构建loginUser
        LoginUser loginUser = this.buildLoginUser(user);
        // 根据用户ID，进行登陆（Sa-Token），并存入Sa-Token的缓存当中，以便于获取当前登录用户信息
        // 生成token，并返回
        LoginHelper.loginByDevice(loginUser, DeviceType.PC);
        log.info("用户登录成功,用户ID:{}", loginUser.getUserId());
        return StpUtil.getTokenValue();
    }

    /**
     * 退出登录（注销）
     */
    public void logout() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        log.info("loginUser:{}", loginUser);
    }


    /**
     * 通过用户名，加载用户信息
     *
     * @param userName 用户名
     * @return {@link SysUser }
     */
    private SysUser loadUserByUsername(String userName) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUserName, userName);

        SysUser loginUser = sysUserMapper.selectOne(lambdaQueryWrapper);

        if (ObjectUtil.isNull(loginUser)) {
            log.info("登录用户：{} 不存在.", userName);
            throw new ServiceException(GlobalErrorCodeConstants.ERROR.getCode(), "登录用户不存在");
        } else if (UserStatus.DISABLE.getCode().equals(loginUser.getStatus())) {
            log.info("登录用户：{} 已被停用.", userName);
            throw new ServiceException(GlobalErrorCodeConstants.ERROR.getCode(), "登录用户被停用");
        }

        return loginUser;
    }

    /**
     * 登录校验
     *
     * @param userName 用户名
     * @param supplier 密码校验结果
     */
    private void checkLogin(String userName, Supplier<Boolean> supplier) {
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + userName;

        // 从Redis中获取用户登录错误次数
        Integer errorNumber = redisCache.getCacheObject(errorKey);

        // 锁定时间内登录，禁止登录
        if (ObjectUtil.isNotNull(errorNumber) && errorNumber.equals(maxRetryCount)) {
            throw new ServiceException(GlobalErrorCodeConstants.ERROR.getCode(), "登录失败，该账户已被锁定");
        }

        // 校验密码是否正确
        if (supplier.get()) {
            // 登录密码，第一次错误（未获取到）
            errorNumber = ObjectUtil.isNull(errorNumber) ? 1 : errorNumber + 1;
            // 达到规定错误次数 则锁定登录
            if (errorNumber.equals(maxRetryCount)) {
                redisCache.setCacheObject(errorKey, errorNumber, lockTime, TimeUnit.MINUTES);
                throw new ServiceException(GlobalErrorCodeConstants.ERROR.getCode(), String.format("密码输入错误%s次，帐户锁定%s分钟", maxRetryCount, lockTime));
            } else {
                // 未达到规定错误次数 则递增
                redisCache.setCacheObject(errorKey, errorNumber);
                throw new ServiceException(GlobalErrorCodeConstants.ERROR.getCode(), String.format("密码输入错误%s次", errorNumber));
            }
        }

        // 登录成功 清空错误次数
        redisCache.deleteObject(errorKey);
    }

    /**
     * 构建登录用户
     *
     * @param user 用户
     * @return {@link LoginUser } 登录用户信息
     */
    private LoginUser buildLoginUser(SysUser user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setUserName(user.getUserName());
        loginUser.setUserType(user.getUserType());
//        loginUser.setMenuPermission(permissionService.getMenuPermission(user));
//        loginUser.setRolePermission(permissionService.getRolePermission(user));
//        List<RoleDTO> roles = BeanUtil.copyToList(user.getRoles(), RoleDTO.class);
//        loginUser.setRoles(roles);
        return loginUser;
    }


}
