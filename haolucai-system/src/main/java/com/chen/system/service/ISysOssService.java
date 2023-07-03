package com.chen.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.model.dto.system.SysOssDTO;
import com.chen.model.entity.PageQuery;
import com.chen.model.entity.system.SysOss;
import com.chen.model.vo.system.SysOssVo;
import com.chen.service.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Pipiing
 * @description 针对表【sys_oss(OSS对象存储表)】的数据库操作Service
 * @createDate 2023-06-23 15:58:45
 */
public interface ISysOssService extends IService<SysOss> {


    /**
     * 文件上传
     *
     * @param file 文件
     * @return {@link SysOssVo } OSS对象存储视图对象
     */
    SysOssVo upload(MultipartFile file);

    /**
     * 文件下载
     *
     * @param ossId    OSS对象ID
     * @param response 响应
     */
    void download(Long ossId, HttpServletResponse response) throws IOException;

    /**
     * 根据OSS对象ID获取OSS视图对象（Vo）
     *
     * @param ossId OSS对象ID
     * @return {@link SysOssVo }
     */
    SysOssVo getVoById(Long ossId);

    /**
     * 根据OSS对象ID串批量删除OSS对象
     *
     * @param ossIds OSS对象ID串
     * @return int 删除成功数目
     */
    int deleteByIds(List<Long> ossIds);

    /**
     * 获取OSS对象存储分页信息列表
     *
     * @param dto       OSS对象存储分页查询DTO对象
     * @param pageQuery 分页条件查询
     * @return {@link TableDataInfo }<{@link SysOssVo }> 分页OSS存储对象信息列表
     */
    TableDataInfo<SysOssVo> queryPageList(SysOssDTO dto, PageQuery pageQuery);
}
