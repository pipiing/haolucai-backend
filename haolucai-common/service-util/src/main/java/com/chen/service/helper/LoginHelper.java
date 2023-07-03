package com.chen.service.helper;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.chen.common.constant.SystemConstants;
import com.chen.common.enums.DeviceType;
import com.chen.model.entity.system.LoginUser;

/**
 * 登录鉴权助手
 *
 * @author Pipiing
 * @description
 * @date 2023/05/24 13:59:32
 */
public class LoginHelper {

    /**
     * 登录用户信息 Key
     */
    public static final String LOGIN_USER_KEY = "loginUser";

    /**
     * 用户ID Key
     */
    public static final String USER_KEY = "userId";

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param loginUser 登录用户信息
     */
    public static void loginByDevice(LoginUser loginUser, DeviceType deviceType) {
        SaStorage storage = SaHolder.getStorage();
        // 将登录用户信息、用户ID 存入Sa-Token缓存中
        storage.set(LOGIN_USER_KEY, loginUser);
        storage.set(USER_KEY, loginUser.getUserId());

        // 将当前设备类型，以扩展形式存入Sa-Token缓存中
        SaLoginModel model = new SaLoginModel();
        if (ObjectUtil.isNotNull(deviceType)) {
            model.setDevice(deviceType.getDevice());
        }
        // 用户登录，并生成Token
        StpUtil.login(loginUser.getUserId(), model.setExtra(USER_KEY, loginUser.getUserId()));
        // 将登录用户信息存入Session中
        StpUtil.getTokenSession().set(LOGIN_USER_KEY, loginUser);
    }

    /**
     * 获取登录用户信息（多级缓存）
     */
    public static LoginUser getLoginUser() {
        // 从Sa-Token的Storage中获取，如果获取不到，则从Session中获取
        LoginUser loginUser = (LoginUser) SaHolder.getStorage().get(LOGIN_USER_KEY);
        if (loginUser != null) {
            return loginUser;
        }
        // 从Session中获取
        loginUser = (LoginUser) StpUtil.getTokenSession().get(LOGIN_USER_KEY);
        // 重新存入Sa-Token的Storage缓存中
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        return loginUser;
    }

    /**
     * 获取登录用户信息（基于token）
     */
    public static LoginUser getLoginUser(String token) {
        // 基于Token获取指定Token-Session
        return (LoginUser) StpUtil.getTokenSessionByToken(token).get(LOGIN_USER_KEY);
    }

    /**
     * 获取登录用户ID
     */
    public static Long getUserId() {
        Long userId;
        try {
            userId = Convert.toLong(SaHolder.getStorage().get(USER_KEY));
            if (ObjectUtil.isNull(userId)) {
                userId = Convert.toLong(StpUtil.getExtra(USER_KEY));
                SaHolder.getStorage().set(USER_KEY, userId);
            }
        } catch (Exception e) {
            return null;
        }
        return userId;
    }

    /**
     * 获取登录用户账户（用户名）
     *
     * @return {@link String } 用户名
     */
    public static String getUsername() {
        return getLoginUser().getUserName();
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return boolean 结果
     */
    public static boolean isAdmin(Long userId) {
        return SystemConstants.ADMIN_ID.equals(userId);
    }

    /**
     * 设置用户数据(多级缓存)
     * 将用户信息 存入token-session缓存（Redis）
     */
    public static void setLoginUser(LoginUser loginUser) {
        StpUtil.getTokenSession().set(LOGIN_USER_KEY, loginUser);
    }
}
