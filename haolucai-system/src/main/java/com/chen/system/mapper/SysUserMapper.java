package com.chen.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.model.entity.system.SysUser;
import com.chen.service.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author chen
 * @description 针对表【sys_user(用户信息表)】的数据库操作Mapper
 * @createDate 2023-05-22 14:28:07
 * @Entity com.chen.model.entity.system.SysUser
 */
@Repository
public interface SysUserMapper extends BaseMapperPlus<SysUserMapper,SysUser,SysUser> {

    /**
     * 根据用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);

    /**
     * 根据用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(Long userId);

    /**
     * 根据条件分页查询已配用户角色列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    Page<SysUser> selectAssignedList(@Param("page") Page<SysUser> build, @Param(Constants.WRAPPER) QueryWrapper<SysUser> queryWrapper);

    /**
     * 根据条件分页查询未配用户角色列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    Page<SysUser> selectUnAssignedList(@Param("page") Page<Object> build, @Param(Constants.WRAPPER) QueryWrapper<SysUser> queryWrapper);

}




