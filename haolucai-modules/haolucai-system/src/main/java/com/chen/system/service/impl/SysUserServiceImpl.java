package com.chen.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.core.constant.GlobalErrorCodeConstants;
import com.chen.common.core.constant.UserConstants;
import com.chen.common.core.exception.ServiceException;
import com.chen.common.mybatis.core.page.PageQuery;
import com.chen.common.mybatis.core.page.TableDataInfo;
import com.chen.model.entity.system.SysUser;
import com.chen.model.entity.system.SysUserRole;
import com.chen.system.mapper.SysUserMapper;
import com.chen.system.mapper.SysUserRoleMapper;
import com.chen.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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
    private SysUserMapper baseMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    public SysUser selectUserById(Long userId) {
        return baseMapper.selectUserById(userId);
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
                .eq(SysUser::getUserName, user.getUserName())
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
        lambdaQueryWrapper.eq(SysUser::getPhone, user.getPhone())
                .ne(ObjectUtil.isNotNull(user.getId()), SysUser::getId, user.getId());
        boolean isExist = baseMapper.exists(lambdaQueryWrapper);

        if (isExist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(SysUser user) {
        if (ObjectUtil.isNull(user.getSex())){
            user.setSex(UserConstants.SEX_UNKNOW);
        }
        // 新增用户信息
        int rows = baseMapper.insert(user);
        // 新增用户与角色信息
        this.insertUserRole(user.getId(), user.getRoleIds());
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(SysUser user) {
        Long userId = user.getId();
        // 删除 用户-角色关联关系
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
        // 新增 用户-角色关联关系
        insertUserRole(user.getId(), user.getRoleIds());
        // 修改用户信息
        return baseMapper.updateById(user);
    }

    @Override
    public void checkUserAllowed(SysUser user) {
        if (ObjectUtil.isNotNull(user.getId()) && user.isAdmin()) {
            throw new ServiceException(GlobalErrorCodeConstants.ERROR.getCode(), "不允许操作管理员用户");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByIds(Long[] userIds) {
        // 将 用户ID数组 转换成 用户ID集合
        List<Long> userIdList = Arrays.asList(userIds);
        // 判断当前用户ID集合中是否包含管理员用户，不允许操作管理员用户
        userIdList.forEach(userId -> checkUserAllowed(new SysUser(userId)));
        // 删除 用户-角色关联关系
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().in(CollUtil.isNotEmpty(userIdList), SysUserRole::getUserId, userIdList));
        // 批量删除用户信息
        return baseMapper.deleteBatchIds(userIdList);
    }

    @Override
    public int resetPwd(SysUser user) {
        return baseMapper.updateById(user);
    }

    @Override
    public int updateUserStatus(SysUser user) {
        return baseMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserAuth(Long userId, Long[] roleIds) {
        // 根据用户ID删除全部 用户-角色关联关系
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
        // 新增 用户-角色关联关系
        this.insertUserRole(userId, roleIds);
    }

    @Override
    public TableDataInfo<SysUser> selectAssignedList(SysUser user, PageQuery pageQuery) {
        QueryWrapper<SysUser> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("u.is_deleted", UserConstants.USER_NORMAL) // 账号未删除
                .eq(ObjectUtil.isNotNull(user.getRoleId()),"r.id",user.getRoleId()) // 已分配角色ID
                .like(StringUtils.isNotBlank(user.getUserName()), "u.user_name", user.getUserName()) // 用户名模糊查询
                .eq(ObjectUtil.isNotNull(user.getStatus()), "u.status", user.getStatus()) // 账号是否被禁用
                .like(StringUtils.isNotBlank(user.getPhone()), "u.phone", user.getPhone()) // 手机号码模糊查询
        ;
        Page<SysUser> page = baseMapper.selectAssignedList(pageQuery.build(), QueryWrapper);
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<SysUser> selectUnAssignedList(SysUser user, PageQuery pageQuery) {
        // 根据角色ID查询拥有该角色的全部用户ID集合
        List<Long> userIds = sysUserRoleMapper.selectUserIdsByRoleId(user.getRoleId());
        QueryWrapper<SysUser> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("u.is_deleted", UserConstants.USER_NORMAL) // 账号未删除
                // AND (r.id <> ? OR r.id IS NULL) 用于缩小数据范围，先将 非当前roleId 和 roleId==Null 的用户取出
                .and(w -> w.ne("r.id", user.getRoleId()).or().isNull("r.id"))
                .notIn(CollUtil.isNotEmpty(userIds),"u.id",userIds) // 将拥有该角色的用户ID全部排除
                .like(StringUtils.isNotBlank(user.getUserName()), "u.user_name", user.getUserName()) // 用户名模糊查询
                .like(StringUtils.isNotBlank(user.getPhone()), "u.phone", user.getPhone()) // 手机号码模糊查询
        ;
        Page<SysUser> page = baseMapper.selectUnAssignedList(pageQuery.build(), QueryWrapper);
        return TableDataInfo.build(page);
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
                .eq(SysUser::getIsDeleted, UserConstants.USER_NORMAL) // 账号未删除
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
     * @param roleIds 角色ID组
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




