package com.chen.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.NodeParser;

import java.util.List;

/**
 * 扩展 hutool TreeUtil 封装系统树构建
 *
 * @author Pipiing
 * @description
 * @date 2023/06/26 10:59:20
 */
public class TreeBuildUtils extends TreeUtil {

    /**
     * 根据前端定制差异化字段
     */
    public static final TreeNodeConfig DEFAULT_CONFIG = TreeNodeConfig.DEFAULT_CONFIG.setNameKey("label");

    /**
     * 构建下拉树结构
     *
     * @param list       列表
     * @param nodeParser 节点解析器 （子节点,Tree）
     * @return {@link List }<{@link Tree }<{@link K }>>
     */
    public static <T, K> List<Tree<K>> build(List<T> list, NodeParser<T, K> nodeParser) {
        // 如果传入构建下拉树集合为空，则直接返回null
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        // 获取list中第一个元素的父ID（父ID经过升序排序才能生效）
        K k = ReflectUtils.invokeGetter(list.get(0), "parentId");
        return TreeUtil.build(list, k, DEFAULT_CONFIG, nodeParser);
    }


}
