package com.chen;

import cn.dev33.satoken.secure.BCrypt;
import com.chen.model.entity.system.LoginUser;
import com.chen.model.entity.system.SysUser;
import com.chen.service.helper.LoginHelper;
import com.chen.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class HaoLuCaiApplicationTest {

    @Autowired
    private SysUserMapper sysUserMapper;

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


}
