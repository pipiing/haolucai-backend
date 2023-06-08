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
    SysUser selectUserById(Long userId);


    /**
     * 获取用户分页信息列表
     *
     * @param user 用户条件查询参数
     * @param pageQuery 分页查询
     * @return {@link TableDataInfo }<{@link SysUser }> 分页用户信息列表
     */
    TableDataInfo<SysUser> selectPageUserList(SysUser user,PageQuery pageQuery);

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return {@link String } 是否唯一结果码
     */
    String checkUserNameUnique(SysUser user);

    /**
     * 校验电话号码是否唯一
     *
     * @param user 用户信息
     * @return {@link String } 是否唯一结果码
     */
    String checkPhoneUnique(SysUser user);

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return int 插入成功数目
     */
    int insertUser(SysUser user);
}
