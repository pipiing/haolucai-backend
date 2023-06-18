package com.chen.system.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.constant.UserConstants;
import com.chen.common.utils.StreamUtils;
import com.chen.model.entity.PageQuery;
import com.chen.model.entity.system.*;
import com.chen.service.exception.ServiceException;
import com.chen.service.exception.enums.GlobalErrorCodeConstants;
import com.chen.service.helper.LoginHelper;
import com.chen.service.page.TableDataInfo;
import com.chen.system.mapper.SysRoleMapper;
import com.chen.system.mapper.SysRoleMenuMapper;
import com.chen.system.mapper.SysUserRoleMapper;
import com.chen.system.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Pipiing
 * @description 针对表【sys_role(角色表)】的数据库操作Service实现
 * @createDate 2023-05-25 09:32:54
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements ISysRoleService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = baseMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (ObjectUtil.isNotNull(perm)) {
                // 将 角色权限字符串 进行按“,”分割 添加到角色权限字符串集合中
                permsSet.addAll(StrUtil.split(perm.getRoleKey().trim(), ","));
            }
        }
        return permsSet;
    }

    public List<SysRole> selectRoleList(SysRole role) {
        return baseMapper.selectList(this.buildQueryWrapper(role));
    }


    @Override
    public List<SysRole> selectRoleAll() {
        return this.selectRoleList(new SysRole());
    }

    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        // TODO RuoYi-Vue-Plus写法，为什么不直接根据用户ID查询拥有的角色对象集合
        // return baseMapper.selectRolePermissionByUserId(userId);

        // 根据用户ID查询角色对象集合
        List<SysRole> userRoles = baseMapper.selectRolePermissionByUserId(userId);
        // 获取全部角色对象集合
        List<SysRole> roles = this.selectRoleAll();
        // 循环判断 用户所拥有的角色集合 和 全部对象集合，将用户所拥有的角色集合标识为true
        for (SysRole userRole : userRoles) {
            for (SysRole role : roles) {
                if (userRole.getId().intValue() == role.getId().intValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    @Override
    public TableDataInfo<SysRole> selectPageRoleList(SysRole role, PageQuery pageQuery) {
        Page<SysRole> page = baseMapper.selectPage(pageQuery.build(), this.buildQueryWrapper(role));
        return TableDataInfo.build(page);
    }

    @Override
    public SysRole selectRoleById(Long roleId) {
        return baseMapper.selectById(roleId);
    }

    @Override
    public String checkRoleNameUnique(SysRole role) {
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 获取除自己以外的的与传入roleName相等的全部角色信息
        lambdaQueryWrapper.eq(SysRole::getRoleName, role.getRoleName())
                .ne(ObjectUtil.isNotNull(role.getId()), SysRole::getId, role.getId());
        boolean isExist = baseMapper.exists(lambdaQueryWrapper);
        if (isExist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public String checkRoleKeyUnique(SysRole role) {
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 获取除自己以外的的与传入roleKey相等的全部角色信息
        lambdaQueryWrapper.eq(SysRole::getRoleKey, role.getRoleKey())
                .ne(ObjectUtil.isNotNull(role.getId()), SysRole::getId, role.getId());
        boolean isExist = baseMapper.exists(lambdaQueryWrapper);
        if (isExist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRole(SysRole role) {
        // 新增角色信息
        int rows = baseMapper.insert(role);
        // 新增角色与菜单信息
        this.insertRoleMenu(role.getId(), role.getMenuIds());
        return rows;
    }

    @Override
    public void checkRoleAllowed(SysRole role) {
        if (ObjectUtil.isNotNull(role.getId()) && role.isAdmin()) {
            throw new ServiceException(GlobalErrorCodeConstants.ERROR.getCode(), "不允许操作管理员角色");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(SysRole role) {
        Long roleId = role.getId();
        // 修改角色信息
        baseMapper.updateById(role);
        // 删除 角色-菜单关联关系
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(ObjectUtil.isNotNull(roleId), SysRoleMenu::getRoleId, roleId));
        // 新增 角色-菜单关联关系
        return this.insertRoleMenu(roleId, role.getMenuIds());
    }

    @Override
    public void cleanOnlineUserByRole(Long roleId) {
        // 查询所有已登录的 Token（在线用户）
        List<String> keys = StpUtil.searchTokenValue("", 0, -1, false);
        if (CollUtil.isEmpty(keys)) {
            // 没有在线用户 无需操作
            return;
        }
        keys.parallelStream().forEach(key -> {
            // Redis存储模式下，searchTokenValue方法返回的是RedisKey（）
            // 获取":"后的字符串 tokenValue
            String token = StringUtils.substringAfterLast(key, ":");
            // 如果已经过期则跳过
            if (StpUtil.stpLogic.getTokenActivityTimeoutByToken(token) < -1) {
                return;
            }
            LoginUser loginUser = LoginHelper.getLoginUser(token);
            // 遍历在线用户的角色权限，如果存在修改的角色ID，则通过token强制注销
            if (loginUser.getRoles().stream().anyMatch(r -> r.getRoleId().equals(roleId))) {
                try {
                    StpUtil.logoutByTokenValue(token);
                } catch (NotLoginException ignored) {
                }
            }
        });

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleByIds(Long[] roleIds) {
        List<Long> roleIdList = Arrays.asList(roleIds);
        for (Long roleId : roleIdList) {
            this.checkRoleAllowed(new SysRole(roleId));
            SysRole role = this.selectRoleById(roleId);
            if (this.countUserRoleByRoleId(roleId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 批量删除 角色-菜单关联关系
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().in(CollUtil.isNotEmpty(roleIdList), SysRoleMenu::getRoleId, roleIdList));
        // 批量删除角色
        return baseMapper.deleteBatchIds(roleIdList);
    }

    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds) {
        // 批量删除 用户-角色关联关系
        int rows = sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId, roleId)
                .in(ArrayUtil.isNotEmpty(userIds), SysUserRole::getUserId, Arrays.asList(userIds))
        );
        // 删除成功 强制注销该角色ID的全部用户
        if (rows > 0) {
            this.cleanOnlineUserByRole(roleId);
        }
        return rows;
    }

    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds) {
        // 批量新增 用户-角色关联关系
        int rows = 1;
        List<SysUserRole> list = StreamUtils.toList(Arrays.asList(userIds), userId -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(roleId);
            sysUserRole.setRoleId(roleId);
            return sysUserRole;
        });
        if (CollUtil.isNotEmpty(list)) {
            rows = sysUserRoleMapper.insertBatch(list) ? list.size() : 0;
        }
        // 新增成功 强制注销该角色ID的全部用户
        if (rows > 0) {
            this.cleanOnlineUserByRole(roleId);
        }
        return rows;
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return long 角色使用数量
     */
    private long countUserRoleByRoleId(Long roleId) {
        return sysUserRoleMapper.selectCount(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId));
    }

    /**
     * 新增角色菜单信息
     * 角色-菜单 关联关系
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID组
     */
    private int insertRoleMenu(Long roleId, Long[] menuIds) {
        // 初始化 rows=1 为了保证修改角色信息成功时，更新Redis缓存中的角色信息
        int rows = 1;
        if (ArrayUtil.isNotEmpty(menuIds)) {
            List<SysRoleMenu> list = new ArrayList<>();
            for (Long menuId : menuIds) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenu.setMenuId(menuId);
                list.add(sysRoleMenu);
            }
            // 批量插入 角色-菜单关联关系
            rows = sysRoleMenuMapper.insertBatch(list) ? list.size() : 0;
        }
        return rows;
    }

    /**
     * 构建条件查询对象
     *
     * @param role 角色信息
     * @return {@link LambdaQueryWrapper }<{@link SysUser }> Lambda查询条件构造器
     */
    private LambdaQueryWrapper<SysRole> buildQueryWrapper(SysRole role) {
        Map<String, Object> params = role.getParams();
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRole::getIsDeleted, UserConstants.ROLE_NORMAL)
                .eq(ObjectUtil.isNotNull(role.getId()), SysRole::getId, role.getId())
                .like(StrUtil.isNotBlank(role.getRoleName()), SysRole::getRoleName, role.getRoleName())
                .eq(StrUtil.isNotBlank(role.getStatus()), SysRole::getStatus, role.getStatus())
                .like(StrUtil.isNotBlank(role.getRoleKey()), SysRole::getRoleKey, role.getRoleKey())
                .between(params.get("beginTime") != null && params.get("endTime") != null,
                        SysRole::getCreateTime, params.get("beginTime"), params.get("endTime"))
                .orderByAsc(SysRole::getRoleSort)
                .orderByAsc(SysRole::getCreateTime);

        return lambdaQueryWrapper;
    }


}




