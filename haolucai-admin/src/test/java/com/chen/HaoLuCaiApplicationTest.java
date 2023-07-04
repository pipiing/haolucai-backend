package com.chen;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.chen.admin.HaoLuCaiApplication;
import com.chen.common.core.constant.GaoDeMapConstants;
import com.chen.common.core.domain.model.LoginUser;
import com.chen.common.core.enums.AreaLevel;
import com.chen.common.mybatis.core.page.PageQuery;
import com.chen.common.mybatis.core.page.TableDataInfo;
import com.chen.common.satoken.helper.LoginHelper;
import com.chen.model.entity.shop.Area;
import com.chen.model.entity.system.SysUser;
import com.chen.shop.mapper.AreaMapper;
import com.chen.system.mapper.SysUserMapper;
import com.chen.system.service.ISysUserService;
import com.chen.system.service.SysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootTest(classes = HaoLuCaiApplication.class)
public class HaoLuCaiApplicationTest {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysPermissionService sysPermissionService;


    @Autowired
    private AreaMapper areaMapper;

    @Test
    public void test() {
        SysUser user = sysUserMapper.selectById(1L);
        log.info("user:{}", user);
    }

    @Test
    public void test2() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        log.info("loginUser:{}", loginUser);
    }

    @Test
    public void test3() {
        String hashpw = BCrypt.hashpw("202428");
        log.info(hashpw);
    }

    @Test
    public void test4() {
        SysUser user = sysUserMapper.selectById(1L);
        Set<String> rolePermission = sysPermissionService.getRolePermission(user);
        log.info("rolePermission:{}", rolePermission);
    }


    @Test
    public void test6() {
        SysUser user = new SysUser();
        user.setRoleId(1L);
        user.setStatus(1);
        TableDataInfo<SysUser> sysUserTableDataInfo = sysUserService.selectAssignedList(user, new PageQuery());
        log.info("sysUserTableDataInfo:{}", sysUserTableDataInfo);
    }

    @Test
    public void test7() {
        SysUser user = new SysUser();
        user.setRoleId(1L);
        user.setStatus(1);
        TableDataInfo<SysUser> sysUserTableDataInfo = sysUserService.selectUnAssignedList(user, new PageQuery());
        log.info("sysUserTableDataInfo:{}", sysUserTableDataInfo);
    }

    /**
     * 获取省市区街道地区信息（高德）
     * 插入数据库
     */
    @Test
    public void test8() {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("key", "3611e5f2fd43fcec8c23722faf84cb46");
        paramMap.put("subdistrict", "10");
        String result = HttpUtil.get(GaoDeMapConstants.ADMINISTRATIVE_DISTRICT_QUERY, paramMap);
        JSONObject jsonObject = JSON.parseObject(result);
        JSONArray arr = jsonObject.getJSONArray("districts");
        JSONArray jsonArray = arr.getJSONObject(0).getJSONArray("districts");
        ArrayList<Area> list = new ArrayList<>();
        // 递归组装AreaList集合
        recursionFn(list, jsonArray, 0L);
        // 批量插入数据库（自动注入报错 未登录）
        areaMapper.insertBatch(list);
    }

    /**
     * 递归列表（可以解决 无限制下级地区 问题）
     *
     * @param list      存储地区对象列表
     * @param jsonArray json数组
     * @param parentId  父地区ID
     */
    private static void recursionFn(List<Area> list, JSONArray jsonArray, Long parentId) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Area area = new Area();
            Long id = IdUtil.getSnowflakeNextId();
            String name = jsonObject.getString("name");
            String level = jsonObject.getString("level");
            // 如果地区等级为省级，则将PID设置为0L
            if (AreaLevel.PROVINCE.getInfo().equals(level)) {
                area.setParentId(0L);
            }
            area.setParentId(parentId);
            area.setId(id);
            area.setAreaName(name);
            area.setLevel(AreaLevel.getAreaLevel(level).getCode());
            list.add(area);
            // 判断当前地区信息是否存在下级地区（递归退出条件）
            if (hasDistricts(jsonObject)) {
                // 向下级递归
                recursionFn(list, getDistricts(jsonObject), id);
            }
        }
    }

    /**
     * 获取子地区列表
     *
     * @param jsonObject json对象
     * @return {@link JSONArray } 子地区json数组
     */
    private static JSONArray getDistricts(JSONObject jsonObject) {
        return jsonObject.getJSONArray("districts");
    }

    /**
     * 判断是否有子地区
     *
     * @param jsonObject json对象
     * @return boolean true:存在子地区数据 false:不存子地区数据
     */
    private static boolean hasDistricts(JSONObject jsonObject) {
        return getDistricts(jsonObject).size() > 0;
    }


}
