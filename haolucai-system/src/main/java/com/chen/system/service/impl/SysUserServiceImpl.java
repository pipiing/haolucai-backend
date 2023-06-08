package com.chen.system.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.constant.UserConstants;
import com.chen.model.entity.PageQuery;
import com.chen.model.entity.system.SysUser;
import com.chen.model.entity.system.SysUserRole;
import com.chen.service.page.TableDataInfo;
import com.chen.system.mapper.SysUserMapper;
import com.chen.system.mapper.SysUserRoleMapper;
import com.chen.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chen
 * @description 针对表【sys_user(用户信息表)】的数据库操作Service实现
 * @createDate 2023-05-22 14:28:07
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements ISysUserService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    public SysUser selectUserById(Long userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public TableDataInfo<SysUser> selectPageUserList(SysUser user, PageQuery pageQuery) {
        Page<SysUser> page = baseMapper.selectPage(pageQuery.build(), this.buildQueryWrapper(user));
        return TableDataInfo.build(page);
    }

    @Override
    public String checkUserNameUnique(SysUser user) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 获取除了自身以外的与传入userName相等的用户信息
        lambdaQueryWrapper
                .eq(StrUtil.isNotBlank(user.getUserName()), SysUser::getUserName, user.getUserName())
                // 修改用户信息时，需执行
                .ne(ObjectUtil.isNotNull(user.getId()), SysUser::getId, user.getId());
        boolean isExist = baseMapper.exists(lambdaQueryWrapper);

        if (isExist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public String checkPhoneUnique(SysUser user) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StrUtil.isNotBlank(user.getPhone()), SysUser::getPhone, user.getPhone())
                .ne(ObjectUtil.isNotNull(user.getId()), SysUser::getId, user.getId());
        boolean isExist = baseMapper.exists(lambdaQueryWrapper);

        if (isExist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = baseMapper.insert(user);
        // 新增用户与角色信息
        this.insertUserRole(user.getId(), user.getRoleIds());
        return rows;
    }

    /**
     * 构建条件查询对象
     *
     * @param user 用户信息
     * @return {@link LambdaQueryWrapper }<{@link SysUser }> Lambda查询条件构造器
     */
    private LambdaQueryWrapper<SysUser> buildQueryWrapper(SysUser user) {
        // 获取其他条件查询参数
        Map<String, Object> params = user.getParams();
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(SysUser::getIsDeleted, UserConstants.USER_NORMAL) // 账号是否被删除
                .eq(ObjectUtil.isNotNull(user.getId()), SysUser::getId, user.getId()) // 用户ID查询
                .like(StrUtil.isNotBlank(user.getUserName()), SysUser::getUserName, user.getUserName()) // 用户名模糊查询
                .eq(ObjectUtil.isNotNull(user.getStatus()), SysUser::getStatus, user.getStatus()) // 账号是否被禁用
                .like(StrUtil.isNotBlank(user.getNickName()), SysUser::getNickName, user.getNickName()) // 用户昵称模糊查询
                .like(StrUtil.isNotBlank(user.getPhone()), SysUser::getPhone, user.getPhone()) // 手机号码模糊查询
                .between(params.get("beginTime") != null && params.get("endTime") != null,
                        SysUser::getCreateTime, params.get("beginTime"), params.get("endTime")); // 创建时间是否在开始时间和结束时间范围内

        return lambdaQueryWrapper;
    }

    /**
     * 新增用户角色信息
     * 用户-角色 关联关系
     *
     * @param userId  用户ID
     * @param roleIds 角色ID集合
     */
    private void insertUserRole(Long userId, Long[] roleIds) {
        if (ArrayUtil.isNotEmpty(roleIds)) {
            // 创建 用户角色关联关系对象 列表
            List<SysUserRole> list = new ArrayList<>(roleIds.length);
            for (Long roleId : roleIds) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(userId);
                sysUserRole.setRoleId(roleId);
                list.add(sysUserRole);
            }
            // 批量插入 用户-角色关联关系
            sysUserRoleMapper.insertBatch(list);
        }
    }


}




