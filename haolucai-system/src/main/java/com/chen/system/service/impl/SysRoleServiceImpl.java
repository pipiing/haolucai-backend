package com.chen.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.constant.UserConstants;
import com.chen.model.entity.system.SysRole;
import com.chen.model.entity.system.SysUser;
import com.chen.system.mapper.SysRoleMapper;
import com.chen.system.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Pipiing
 * @description 针对表【sys_role(角色表)】的数据库操作Service实现
 * @createDate 2023-05-25 09:32:54
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements ISysRoleService {


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

    public List<SysRole> selectRoleList(SysRole role){
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
                if (userRole.getId().intValue() == role.getId().intValue()){
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
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




