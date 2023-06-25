package com.chen.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.model.entity.oss.SysOss;
import com.chen.model.vo.oss.SysOssVo;
import org.springframework.web.multipart.MultipartFile;

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
}
