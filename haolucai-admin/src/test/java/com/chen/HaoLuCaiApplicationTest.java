package com.chen;

import cn.dev33.satoken.secure.BCrypt;
import com.chen.model.entity.system.LoginUser;
import com.chen.model.entity.system.SysUser;
import com.chen.service.helper.LoginHelper;
import com.chen.system.mapper.SysUserMapper;
import com.chen.system.service.ISysRoleService;
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
    private ISysRoleService sysRoleService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Test
    public void test() {
        SysUser user = sysUserMapper.selectById(1661200402135846913L);
        boolean checkpw = BCrypt.checkpw("202428", user.getPassword());
        if (checkpw) {
            log.info("密码一致:{}", true);
        } else {
            log.info("密码不一致:{}", false);
        }
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


}
