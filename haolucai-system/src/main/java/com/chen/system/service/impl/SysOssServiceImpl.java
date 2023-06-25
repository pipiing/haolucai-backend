package com.chen.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.model.entity.oss.SysOss;
import com.chen.model.vo.oss.SysOssVo;
import com.chen.oss.client.OssClient;
import com.chen.oss.entity.UploadResult;
import com.chen.oss.enums.AccessPolicyType;
import com.chen.oss.factory.OssFactory;
import com.chen.service.exception.ServiceException;
import com.chen.system.convert.SysOssConvert;
import com.chen.system.mapper.SysOssMapper;
import com.chen.system.service.ISysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
* @author Pipiing
* @description 针对表【sys_oss(OSS对象存储表)】的数据库操作Service实现
* @createDate 2023-06-23 15:58:45
*/
@Service
public class SysOssServiceImpl extends ServiceImpl<SysOssMapper, SysOss>
    implements ISysOssService {

    @Autowired
    private SysOssMapper baseMapper;

    @Autowired
    private SysOssConvert sysOssConvert;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysOssVo upload(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        // 获取文件后缀名
        String suffix = StrUtil.subSuf(originalFileName, originalFileName.lastIndexOf("."));
        // 获取OSS实例对象
        OssClient storage = OssFactory.instance();
        UploadResult uploadResult;
        try {
            // 携带文件后缀上传文件（文件->字节数组）
            uploadResult = storage.uploadSuffix(file.getBytes(), suffix, file.getContentType());
        } catch (Exception e) {
            log.error("上传文件失败");
            throw new ServiceException(e.getMessage());
        }
        // 生成文件信息
        SysOss oss = new SysOss();
        oss.setUrl(uploadResult.getUrl());
        oss.setFileSuffix(suffix);
        oss.setFileName(uploadResult.getFilename());
        oss.setOriginalName(originalFileName);
        oss.setService(storage.getConfigKey());
        // 插入OSS对象存储表
        baseMapper.insert(oss);
        // 生成OSS存储视图对象（用于返回前端）
        SysOssVo ossVo = sysOssConvert.ossToVo(oss);
        return this.matchingUrl(ossVo);
    }

    /**
     * 匹配Url
     *
     * @param oss OSS对象
     * @return oss 匹配Url的OSS对象
     */
    private SysOssVo matchingUrl(SysOssVo oss) {
        OssClient storage = OssFactory.instance(oss.getService());
        // 仅修改桶类型为 private 的URL，临时URL时长为120s
        if (AccessPolicyType.PRIVATE == storage.getAccessPolicy()) {
            oss.setUrl(storage.getPrivateUrl(oss.getFileName(), 120));
        }
        return oss;
    }

}




