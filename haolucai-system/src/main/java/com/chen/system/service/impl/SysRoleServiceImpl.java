package com.chen.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.model.entity.system.SysRole;
import com.chen.system.mapper.SysRoleMapper;
import com.chen.system.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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
                permsSet.addAll(StrUtil.split(perm.getRoleKey(), ","));
            }
        }
        return permsSet;
    }





}




