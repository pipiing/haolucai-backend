package com.chen.shop.service;


import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.model.entity.shop.Area;

import java.util.List;

/**
 * @author Pipiing
 * @description 针对表【area(地区表)】的数据库操作Service
 * @createDate 2023-06-28 07:46:02
 */
public interface IAreaService extends IService<Area> {

    /**
     * 查询地区列表
     *
     * @param area 地区信息
     * @return {@link List }<{@link Area }> 地区列表
     */
    List<Area> selectAreaList(Area area);

    /**
     * 构建地区下拉树列表
     *
     * @param areaList 地区列表
     * @return {@link List }<{@link Tree }<{@link Long }>> 下拉树结构列表
     */
    List<Tree<Long>> buildAreaTreeSelect(List<Area> areaList);

    /**
     * 根据父级ID获取下级地区列表
     *
     * @param pid pid 父地区ID
     * @return {@link List }<{@link Area }> 下级地区列表
     */
    List<Area> selectChildAreaListByPid(Long pid);

    /**
     * 根据地区ID查询地区信息
     *
     * @param areaId 地区ID
     * @return {@link Area } 地区信息
     */
    Area selectAreaById(Long areaId);

    /**
     * 新增地区信息
     *
     * @param area 地区信息
     * @return int 插入成功数目
     */
    int insertArea(Area area);

    /**
     * 校验当前pid下地区名称是否唯一
     *
     * @param area 地区信息
     * @return boolean true:不唯一 false:唯一
     */
    boolean checkAreaNameUnique(Area area);
}
