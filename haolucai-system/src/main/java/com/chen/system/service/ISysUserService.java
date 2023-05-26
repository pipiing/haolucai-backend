package com.chen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.model.entity.PageQuery;
import com.chen.model.entity.system.SysUser;
import com.chen.service.page.TableDataInfo;

/**
* @author chen
* @description 针对表【sys_user(用户信息表)】的数据库操作Service
* @createDate 2023-05-22 14:28:07
*/
public interface ISysUserService extends IService<SysUser> {

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return {@link SysUser } 用户信息
     */
    SysUser getSysUserByUserId(Long userId);


    /**
     * 获取用户分页信息列表
     *
     * @param user 用户条件查询参数
     * @param pageQuery 分页查询
     * @return {@link TableDataInfo }<{@link SysUser }> 分页用户信息列表
     */
    TableDataInfo<SysUser> selectPageUserList(SysUser user,PageQuery pageQuery);
}
