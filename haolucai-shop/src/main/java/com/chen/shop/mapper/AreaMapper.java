package com.chen.shop.mapper;


import com.chen.model.entity.shop.Area;
import com.chen.service.mapper.BaseMapperPlus;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author Pipiing
* @description 针对表【area(地区表)】的数据库操作Mapper
* @createDate 2023-06-28 07:46:02
* @Entity com.chen.model.Area
*/
@Repository
public interface AreaMapper extends BaseMapperPlus<AreaMapper,Area,Area> {

    /**
     * 根据地区名称模糊查询地区级别最低级地区信息
     *
     * @param areaName 地区名称
     * @return {@link List }<{@link Area }> 地区级别最低级地区信息列表
     */
    List<Area> selectMinLevelAreaListByAreaName(String areaName);
}




