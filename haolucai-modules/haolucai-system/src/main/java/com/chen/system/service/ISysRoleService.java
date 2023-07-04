package com.chen.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.mybatis.core.page.PageQuery;
import com.chen.common.mybatis.core.page.TableDataInfo;
import com.chen.model.entity.system.SysRole;
import com.chen.model.entity.system.SysUser;

import java.util.List;
import java.util.Set;

/**
 * @author Pipiing
 * @description 针对表【sys_role(角色表)】的数据库操作Service
 * @createDate 2023-05-25 09:32:54
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return {@link Set }<{@link String }> 角色权限字符串集合
     */
    Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * 查询所有角色对象集合
     *
     * @return {@link List }<{@link SysRole }> 角色对象集合
     */
    List<SysRole> selectRoleAll();

    /**
     * 根据用户ID查询角色集合
     *
     * @param userId 用户id
     * @return {@link List }<{@link SysRole }> 角色对象集合
     */
    List<SysRole> selectRolesByUserId(Long userId);

    /**
     * 获取角色分页信息列表
     *
     * @param role      角色条件查询参数
     * @param pageQuery 分页查询
     * @return {@link TableDataInfo }<{@link SysUser }> 分页角色信息列表
     */
    TableDataInfo<SysRole> selectPageRoleList(SysRole role, PageQuery pageQuery);

    /**
     * 根据角色ID查询角色信息
     *
     * @param roleId 角色ID
     * @return {@link SysRole } 角色信息
     */
    SysRole selectRoleById(Long roleId);

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return {@link String } 是否唯一结果码
     */
    String checkRoleNameUnique(SysRole role);

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return {@link String } 是否唯一结果码
     */
    String checkRoleKeyUnique(SysRole role);

    /**
     * 新增角色信息
     *
     * @param role 角色信息
     * @return int 插入成功数目
     */
    int insertRole(SysRole role);

    /**
     * 校验角色是否允许操作
     *
     * @param role 用户信息
     */
    void checkRoleAllowed(SysRole role);

    /**
     * 修改角色信息
     *
     * @param role 角色信息
     * @return int 修改成功数目
     */
    int updateRole(SysRole role);

    /**
     * 通过角色ID强制注销在线用户（强制注销在线且拥有该角色权限的用户）
     *
     * @param roleId 角色ID
     */
    void cleanOnlineUserByRole(Long roleId);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 角色ID组
     * @return int 删除成功数目
     */
    int deleteRoleByIds(Long[] roleIds);

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要取消授权的用户ID组
     * @return int 删除成功数目
     */
    int deleteAuthUsers(Long roleId, Long[] userIds);

    /**
     * 批量选择用户授权
     *
     * @param roleId  角色ID
     * @param userIds 需要授权的用户ID组
     * @return int 插入成功数目
     */
    int insertAuthUsers(Long roleId, Long[] userIds);
}
