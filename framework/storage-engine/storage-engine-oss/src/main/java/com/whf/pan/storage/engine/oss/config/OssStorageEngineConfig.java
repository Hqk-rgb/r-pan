package com.whf.pan.storage.engine.oss.config;

import com.aliyun.oss.OSSClient;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.whf.pan.core.exception.FrameworkException;

/**
 * @author whf
 * @className OssStorageEngineConfig
 * @description OSS文件存储引擎配置类
 * @date 2023/11/25 13:10
 */
@Component
@Data
@ConfigurationProperties(prefix = "com.whf.pan.storage.engine.oss")
public class OssStorageEngineConfig {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    private Boolean autoCreateBucket = Boolean.TRUE;

    /**
     * 注入OSS操作客户端对象
     *
     * @return OSSClient
     */
    @Bean(destroyMethod = "shutdown")
    public OSSClient ossClient() {
        if (StringUtils.isAnyBlank(getEndpoint(), getAccessKeyId(), getAccessKeySecret(), getBucketName())) {
            throw new FrameworkException("the oss config is missed!");
        }
        return new OSSClient(getEndpoint(), getAccessKeyId(), getAccessKeySecret());
    }
}
