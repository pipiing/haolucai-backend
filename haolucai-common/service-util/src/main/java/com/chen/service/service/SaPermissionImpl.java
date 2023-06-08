package com.chen.service.service;

import cn.dev33.satoken.stp.StpInterface;
import com.chen.model.entity.system.LoginUser;
import com.chen.service.helper.LoginHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限认证接口扩展
 * Sa-Token权限管理实现类
 * 将从此实现类获取每个账号拥有的权限码
 *
 * @author Pipiing
 * @description
 * @date 2023/06/05 12:21:17
 */
@Slf4j
@Component
public class SaPermissionImpl implements StpInterface {

    /**
     * 获取菜单权限列表
     *
     * @param loginId   登录用户ID
     * @param loginType 登录类型（login）
     * @return {@link List }<{@link String }> 菜单权限集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        return new ArrayList<>(loginUser.getMenuPermission());
    }

    /**
     * 获取角色权限列表
     *
     * @param loginId   登录用户ID
     * @param loginType 登录类型
     * @return {@link List }<{@link String }> 角色权限列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        return new ArrayList<>(loginUser.getRolePermission());
    }
}
