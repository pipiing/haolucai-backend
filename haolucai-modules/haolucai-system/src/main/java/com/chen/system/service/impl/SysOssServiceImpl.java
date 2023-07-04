package com.chen.system.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.core.exception.ServiceException;
import com.chen.common.core.utils.FileUtils;
import com.chen.common.core.utils.MapstructUtils;
import com.chen.common.core.utils.StreamUtils;
import com.chen.common.mybatis.core.page.PageQuery;
import com.chen.common.mybatis.core.page.TableDataInfo;
import com.chen.common.oss.client.OssClient;
import com.chen.common.oss.entity.UploadResult;
import com.chen.common.oss.enums.AccessPolicyType;
import com.chen.common.oss.exception.OssException;
import com.chen.common.oss.factory.OssFactory;
import com.chen.model.dto.system.SysOssDTO;
import com.chen.model.entity.system.SysOss;
import com.chen.model.vo.system.SysOssVo;
import com.chen.system.mapper.SysOssMapper;
import com.chen.system.service.ISysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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


    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysOssVo upload(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (StrUtil.isBlank(originalFileName)) {
            throw new OssException("原文件名为空");
        }
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
        SysOssVo ossVo = MapstructUtils.convert(oss,SysOssVo.class);
        // 将url生成外链（可供外网访问）
        return this.matchingUrl(ossVo);
    }

    @Override
    public void download(Long ossId, HttpServletResponse response) throws IOException {
        SysOssVo sysOss = this.getVoById(ossId);
        if (ObjectUtil.isNull(sysOss)) {
            throw new ServiceException("文件数据不存在!");
        }
        // 将文件变为下载编码且将原始文件名还原
        FileUtils.setAttachmentResponseHeader(response, sysOss.getOriginalName());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8");
        OssClient storage = OssFactory.instance();
        try (InputStream inputStream = storage.getObjectContent(sysOss.getUrl())) {
            // 获取字节输入流实际大小
            int available = inputStream.available();
            // 将该文件得字节输入流 复制到 响应字节输出流中
            IoUtil.copy(inputStream, response.getOutputStream(), available);
            response.setContentLength(available);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public SysOssVo getVoById(Long ossId) {
        SysOss oss = baseMapper.selectById(ossId);
        return MapstructUtils.convert(oss,SysOssVo.class);
    }

    @Override
    public int deleteByIds(List<Long> ossIds) {
        // 根据 OSS对象ID串 获取对应OSS对象列表
        List<SysOss> list = baseMapper.selectBatchIds(ossIds);
        for (SysOss sysOss : list) {
            // 根据每个 OSS对象的服务商 获取不同的对应OSS客户端实例
            OssClient storage = OssFactory.instance(sysOss.getService());
            storage.delete(sysOss.getFileName());
        }
        return baseMapper.deleteBatchIds(ossIds);
    }

    @Override
    public TableDataInfo<SysOssVo> queryPageList(SysOssDTO dto, PageQuery pageQuery) {
        Page<SysOssVo> page = baseMapper.selectVoPage(pageQuery.build(), this.buildQueryWrapper(dto));
        // 将OSS对象存储视图对象列表 中的url 进行外链操作
        List<SysOssVo> ossVoList = StreamUtils.toList(page.getRecords(), this::matchingUrl);
        page.setRecords(ossVoList);
        return TableDataInfo.build(page);
    }

    /**
     * 构建条件查询对象
     *
     * @param dto OSS对象存储分页查询DTO对象
     * @return {@link LambdaQueryWrapper }<{@link SysOss }> Lambda查询条件构造器
     */
    private LambdaQueryWrapper<SysOss> buildQueryWrapper(SysOssDTO dto) {
        Map<String, Object> params = dto.getParams();
        LambdaQueryWrapper<SysOss> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StrUtil.isNotBlank(dto.getFileName()), SysOss::getFileName, dto.getFileName())
                .like(StrUtil.isNotBlank(dto.getOriginalName()), SysOss::getOriginalName, dto.getOriginalName())
                .eq(StrUtil.isNotBlank(dto.getFileSuffix()), SysOss::getFileSuffix, dto.getFileSuffix())
                .eq(StrUtil.isNotBlank(dto.getUrl()), SysOss::getUrl, dto.getUrl())
                .between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
                        SysOss::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"))
                .eq(StrUtil.isNotBlank(dto.getCreateBy()), SysOss::getCreateBy, dto.getCreateBy())
                .eq(StrUtil.isNotBlank(dto.getService()), SysOss::getService, dto.getService())
        ;
        return lambdaQueryWrapper;
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




