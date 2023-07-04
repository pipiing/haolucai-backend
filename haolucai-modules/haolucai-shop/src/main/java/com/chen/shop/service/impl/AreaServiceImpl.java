package com.chen.shop.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.core.constant.CacheConstants;
import com.chen.common.core.constant.GlobalErrorCodeConstants;
import com.chen.common.core.enums.AreaLevel;
import com.chen.common.core.exception.ServiceException;
import com.chen.common.core.utils.TreeBuildUtils;
import com.chen.model.entity.shop.Area;
import com.chen.shop.mapper.AreaMapper;
import com.chen.shop.service.IAreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Pipiing
 * @description 针对表【area(地区表)】的数据库操作Service实现
 * @createDate 2023-06-28 07:46:02
 */
@Slf4j
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area>
        implements IAreaService {

    @Autowired
    private AreaMapper areaMapper;


    @Override
    public List<Area> selectAreaList(Area area) {
        if (StrUtil.isNotBlank(area.getAreaName())) {
            // 模糊查询 地区名称 不为空，且Pid为空，模糊查询地区名称的最低级地区信息
            List<Area> areaList = areaMapper.selectMinLevelAreaListByAreaName(area.getAreaName());
            return this.buildAreaList(areaList);
        }
        if (ObjectUtil.isNull(area.getParentId())) {
            throw new ServiceException(GlobalErrorCodeConstants.PARAM_ERROR);
        }
        return this.selectChildAreaListByPid(area.getParentId());
    }

    @Override
    public List<Tree<Long>> buildAreaTreeSelect(List<Area> areaList) {
        if (CollUtil.isEmpty(areaList)) {
            return CollUtil.newArrayList();
        }
        return TreeBuildUtils.build(areaList, (area, tree) -> {
            tree.setId(area.getId());
            tree.setParentId(area.getParentId());
            tree.setName(area.getAreaName());
        });
    }

    @Override
    @Cacheable(cacheNames = CacheConstants.AREA, key = "#pid") // 将查询下级地区列表数据存入Redis（Hash）
    public List<Area> selectChildAreaListByPid(Long pid) {
        return areaMapper.selectList(new LambdaQueryWrapper<Area>().eq(Area::getParentId, pid));
    }

    @Override
    public Area selectAreaById(Long areaId) {
        return areaMapper.selectById(areaId);
    }

    @Override
    @CacheEvict(cacheNames = "area", key = "#area.parentId") // 新增当前地区时，并删除当前pid的缓存（数据更新）
    public int insertArea(Area area) {
        return areaMapper.insert(area);
    }

    @Override
    public boolean checkAreaNameUnique(Area area) {
        // 根据父级ID获取下级地区列表，且判断是否存在相同地区名称
        return areaMapper.exists(new LambdaQueryWrapper<Area>()
                .eq(Area::getParentId, area.getParentId())
                .eq(Area::getAreaName, area.getAreaName())
                .ne(ObjectUtil.isNotNull(area.getId()), Area::getId, area.getId())
        );
    }

    /**
     * 构建模糊查询地区列表（从最低级别 -> 省级）
     *
     * @param areaList 模糊查询最低级地区列表
     * @return {@link List }<{@link Area }> 地区信息列表（模糊查询）
     */
    private List<Area> buildAreaList(List<Area> areaList) {
        List<Area> returnList = new ArrayList<>();
        // 按ParentId进行分组
        Map<Long, List<Area>> groupAreaMap = areaList.stream().collect(Collectors.groupingBy(Area::getParentId));
        for (Long parentId : groupAreaMap.keySet()) {
            Area provinceArea = this.recursionFn(groupAreaMap.get(parentId), parentId);
            returnList.add(provinceArea);
        }
        return returnList;
    }

    /**
     * 向上递归封装省级地区信息（封装children）
     *
     * @param childAreaList 子地区列表
     * @param parentId      父地区ID
     * @return {@link Area } 省级地区信息
     */
    private Area recursionFn(List<Area> childAreaList, Long parentId) {
        // 根据父ID获取父节点
        Area parentArea = this.selectAreaById(parentId);
        // 将子节点列表组装到父节点的children中
        parentArea.setChildren(childAreaList);
        // 判断当前地区是否存在父地区
        if (this.hasParent(parentArea.getLevel())) {
            // 递归封装
            return this.recursionFn(CollUtil.newArrayList(parentArea), parentArea.getParentId());
        }
        return parentArea;
    }

    /**
     * 是否存在父节点
     *
     * @param level 地区级别
     * @return boolean
     */
    private boolean hasParent(String level) {
        return !AreaLevel.PROVINCE.getCode().equals(level);
    }


}




