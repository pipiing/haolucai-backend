package com.chen.system.mapper;

import com.chen.model.entity.system.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author chen
 * @description 针对表【sys_user(用户信息表)】的数据库操作Mapper
 * @createDate 2023-05-22 14:28:07
 * @Entity com.chen.model.entity.system.SysUser
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

}




