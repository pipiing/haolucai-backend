package com.chen.common.oss.client;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.Protocol;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.chen.common.core.exception.ServerException;
import com.chen.common.core.utils.DateUtils;
import com.chen.common.oss.constant.OssConstant;
import com.chen.common.oss.entity.UploadResult;
import com.chen.common.oss.enums.AccessPolicyType;
import com.chen.common.oss.enums.PolicyType;
import com.chen.common.oss.exception.OssException;
import com.chen.common.oss.properties.OssProperties;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * Oss 客户端
 * S3 存储协议 所有兼容S3协议的云厂商均支持
 * 阿里云 腾讯云 七牛云 minio
 *
 * @author Pipiing
 * @description
 * @date 2023/06/25 10:16:50
 */
public class OssClient {

    private final String configKey;

    private final OssProperties properties;

    private final AmazonS3 client;

    public OssClient(String configKey, OssProperties ossProperties) {
        this.configKey = configKey;
        this.properties = ossProperties;
        try {
            // 创建 AmazonRedshift 客户端
            AwsClientBuilder.EndpointConfiguration endpointConfig =
                    new AwsClientBuilder.EndpointConfiguration(properties.getEndpoint(), properties.getRegion());
            // 生成临时凭证
            AWSCredentials credentials = new BasicAWSCredentials(properties.getAccessKey(), properties.getSecretKey());
            AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
            // 配置客户端
            ClientConfiguration clientConfig = new ClientConfiguration();
            // 通过配置信息，设置Https or Http协议
            if (OssConstant.IS_HTTPS.equals(properties.getIsHttps())) {
                clientConfig.setProtocol(Protocol.HTTPS);
            } else {
                clientConfig.setProtocol(Protocol.HTTP);
            }
            // 构建客户端
            AmazonS3ClientBuilder build = AmazonS3Client.builder()
                    .withEndpointConfiguration(endpointConfig)
                    .withClientConfiguration(clientConfig)
                    .withCredentials(credentialsProvider)
                    .disableChunkedEncoding();
            if (!StringUtils.containsAny(properties.getEndpoint(), OssConstant.CLOUD_SERVICE)) {
                // minio 使用https限制使用域名访问 需要此配置 站点填域名
                build.enablePathStyleAccess();
            }
            this.client = build.build();
            // 创建Bucket
            this.createBucket();
        } catch (Exception e) {
            if (e instanceof ServerException) {
                throw e;
            }
            throw new OssException("配置错误! 请检查系统配置:[" + e.getMessage() + "]");
        }
    }

    /**
     * 创建Bucket
     */
    public void createBucket() {
        try {
            String bucketName = properties.getBucketName();
            // Bucket已存在，直接返回
            if (client.doesBucketExistV2(bucketName)) {
                return;
            }
            // Bucket不存在，根据配置信息中的BucketName创建
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            AccessPolicyType accessPolicy = getAccessPolicy();
            createBucketRequest.setCannedAcl(accessPolicy.getAcl());
            client.createBucket(createBucketRequest);
            client.setBucketPolicy(bucketName, getPolicy(bucketName, accessPolicy.getPolicyType()));
        } catch (SdkClientException e) {
            throw new OssException("创建Bucket失败, 请核对配置信息:[" + e.getMessage() + "]");
        }
    }

    /**
     * 检查配置是否相同
     */
    public boolean checkPropertiesSame(OssProperties properties) {
        return this.properties.equals(properties);
    }

    /**
     * 获取当前桶权限类型
     *
     * @return 当前桶权限类型code
     */
    public AccessPolicyType getAccessPolicy() {
        return AccessPolicyType.getByType(properties.getAccessPolicy());
    }

    /**
     * 携带文件后缀上传文件
     *
     * @param data        数据
     * @param suffix      后缀
     * @param contentType 内容类型
     * @return {@link UploadResult } 上传返回实体
     */
    public UploadResult uploadSuffix(byte[] data, String suffix, String contentType) {
        return this.upload(data, this.getPath(properties.getPrefix(), suffix), contentType);
    }

    /**
     * 使用字节数组上传文件
     *
     * @param data        数据
     * @param path        路径
     * @param contentType 内容类型
     * @return {@link UploadResult } 上传返回实体
     */
    public UploadResult upload(byte[] data, String path, String contentType) {
        return this.upload(new ByteArrayInputStream(data), path, contentType);
    }

    /**
     * 使用输入流上传文件
     *
     * @param inputStream 输入流
     * @param path        路径
     * @param contentType 内容类型
     * @return {@link UploadResult } 上传返回实体
     */
    public UploadResult upload(InputStream inputStream, String path, String contentType) {
        // 传入输入流 不属于 字节数组输入流，则转换为字节数组输入流
        if (!(inputStream instanceof ByteArrayInputStream)) {
            // 将输入流读取为字节数组，生成字节数组输入流
            inputStream = new ByteArrayInputStream(IoUtil.readBytes(inputStream));
        }
        try {
            // 创建元数据对象实体（AWS-S3包）
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            // inputStream.available()：获取流实际大小
            metadata.setContentLength(inputStream.available());
            // 生成上传文件请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(properties.getBucketName(), path, inputStream, metadata);
            // 设置上传对象的 Acl 为公共读
            putObjectRequest.setCannedAcl(this.getAccessPolicy().getAcl());
            // 上传文件
            client.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new OssException("上传文件失败，请检查配置信息:[" + e.getMessage() + "]");
        }
        return UploadResult.builder().url(this.getUrl() + "/" + path).filename(path).build();
    }

    /**
     * 根据文件名称删除OSS对象以及真实文件
     *
     * @param fileName 文件名称
     */
    public void delete(String fileName) {
        // 没有在数据库中存储OSS对象信息，则生成Url链接，将真实存储文件路径拆分
        // path = path.replace(this.getUrl() + "/", "");
        try {
            client.deleteObject(properties.getBucketName(), fileName);
        } catch (Exception e) {
            throw new OssException("删除文件失败，请检查配置信息:[" + e.getMessage() + "]");
        }
    }

    /**
     * 生成存储路径
     *
     * @param prefix 前缀
     * @param suffix 后缀
     * @return {@link String }
     */
    public String getPath(String prefix, String suffix) {
        // 生成uuid
        String uuid = IdUtil.fastSimpleUUID();
        // 文件路径
        String path = DateUtils.datePath() + "/" + uuid;
        if (StrUtil.isNotBlank(prefix)) {
            path = prefix + "/" + path;
        }
        return path + suffix;
    }

    /**
     * 获取文件存储url
     *
     * @return {@link String } 文件存储url
     */
    public String getUrl() {
        String domain = properties.getDomain();
        String endpoint = properties.getEndpoint();
        String header = OssConstant.IS_HTTPS.equals(properties.getIsHttps()) ? "https://" : "http://";
        // 云服务商直接返回
        if (StringUtils.containsAny(endpoint, OssConstant.CLOUD_SERVICE)) {
            if (StringUtils.isNotBlank(domain)) {
                return header + domain;
            }
            return header + properties.getBucketName() + "." + endpoint;
        }
        // minio 单独处理
        if (StringUtils.isNotBlank(domain)) {
            return header + domain + "/" + properties.getBucketName();
        }
        return header + endpoint + "/" + properties.getBucketName();
    }

    /**
     * 获取配置Key
     *
     * @return {@link String } 配置Key
     */
    public String getConfigKey() {
        return configKey;
    }

    /**
     * 获取私有URL链接
     *
     * @param objectKey 对象KEY
     * @param second    授权时间
     */
    public String getPrivateUrl(String objectKey, Integer second) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(properties.getBucketName(), objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(new Date(System.currentTimeMillis() + 1000L * second));
        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    /**
     * 根据文件路径获得文件对象内容（字节输入流）
     *
     * @param path 路径
     * @return {@link InputStream } 字节输入流
     */
    public InputStream getObjectContent(String path) {
        path = path.replace(getUrl() + "/", "");
        S3Object object = client.getObject(properties.getBucketName(), path);
        return object.getObjectContent();
    }

    private static String getPolicy(String bucketName, PolicyType policyType) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n\"Statement\": [\n{\n\"Action\": [\n");
        if (policyType == PolicyType.WRITE) {
            builder.append("\"s3:GetBucketLocation\",\n\"s3:ListBucketMultipartUploads\"\n");
        } else if (policyType == PolicyType.READ_WRITE) {
            builder.append("\"s3:GetBucketLocation\",\n\"s3:ListBucket\",\n\"s3:ListBucketMultipartUploads\"\n");
        } else {
            builder.append("\"s3:GetBucketLocation\"\n");
        }
        builder.append("],\n\"Effect\": \"Allow\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("\"\n},\n");
        if (policyType == PolicyType.READ) {
            builder.append("{\n\"Action\": [\n\"s3:ListBucket\"\n],\n\"Effect\": \"Deny\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
            builder.append(bucketName);
            builder.append("\"\n},\n");
        }
        builder.append("{\n\"Action\": ");
        switch (policyType) {
            case WRITE:
                builder.append("[\n\"s3:AbortMultipartUpload\",\n\"s3:DeleteObject\",\n\"s3:ListMultipartUploadParts\",\n\"s3:PutObject\"\n],\n");
                break;
            case READ_WRITE:
                builder.append("[\n\"s3:AbortMultipartUpload\",\n\"s3:DeleteObject\",\n\"s3:GetObject\",\n\"s3:ListMultipartUploadParts\",\n\"s3:PutObject\"\n],\n");
                break;
            default:
                builder.append("\"s3:GetObject\",\n");
                break;
        }
        builder.append("\"Effect\": \"Allow\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("/*\"\n}\n],\n\"Version\": \"2012-10-17\"\n}\n");
        return builder.toString();
    }


}
