package com.chen;

import cn.dev33.satoken.secure.BCrypt;
import com.chen.common.utils.RedisUtils;
import com.chen.model.dto.system.SysRoleDTO;
import com.chen.model.entity.PageQuery;
import com.chen.model.entity.system.LoginUser;
import com.chen.model.entity.system.SysRole;
import com.chen.model.entity.system.SysUser;
import com.chen.service.helper.LoginHelper;
import com.chen.service.page.TableDataInfo;
import com.chen.system.convert.SysRoleConvert;
import com.chen.system.mapper.SysUserMapper;
import com.chen.system.service.ISysUserService;
import com.chen.system.service.SysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@Slf4j
@SpringBootTest
public class HaoLuCaiApplicationTest {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Autowired
    private SysRoleConvert sysRoleConvert;

    @Test
    public void test() {
        SysUser user = sysUserMapper.selectById(1L);
        RedisUtils.setCacheObject("user", user);
    }

    @Test
    public void test2(){
        LoginUser loginUser = LoginHelper.getLoginUser();
        log.info("loginUser:{}", loginUser);
    }

    @Test
    public void test3(){
        String hashpw = BCrypt.hashpw("202428");
        log.info(hashpw);
    }

    @Test
    public void test4(){
        SysUser user = sysUserMapper.selectById(1L);
        Set<String> rolePermission = sysPermissionService.getRolePermission(user);
        log.info("rolePermission:{}", rolePermission);
    }

    @Test
    public void test5(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("admin");
        SysRoleDTO sysRoleDTO = sysRoleConvert.roleToDTO(sysRole);
        log.info("RoleDTO:{}", sysRoleDTO);
    }

    @Test
    public void test6(){
        SysUser user = new SysUser();
        user.setRoleId(1L);
        user.setStatus(1);
        TableDataInfo<SysUser> sysUserTableDataInfo = sysUserService.selectAssignedList(user, new PageQuery());
        log.info("sysUserTableDataInfo:{}", sysUserTableDataInfo);
    }

    @Test
    public void test7(){
        SysUser user = new SysUser();
        user.setRoleId(1L);
        user.setStatus(1);
        TableDataInfo<SysUser> sysUserTableDataInfo = sysUserService.selectUnAssignedList(user, new PageQuery());
        log.info("sysUserTableDataInfo:{}", sysUserTableDataInfo);
    }




}
