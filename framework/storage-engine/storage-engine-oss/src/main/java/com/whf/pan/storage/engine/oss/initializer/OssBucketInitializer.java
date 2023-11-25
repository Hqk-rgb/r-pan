package com.whf.pan.storage.engine.oss.initializer;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.whf.pan.core.exception.FrameworkException;
import com.whf.pan.storage.engine.oss.config.OssStorageEngineConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author whf
 * @className OssBucketInitializer
 * @description OSS桶初始化器
 * @date 2023/11/25 13:11
 */
@Component
@Slf4j
public class OssBucketInitializer implements CommandLineRunner {
    @Resource
    private OssStorageEngineConfig config;

    @Resource
    private OSSClient client;

    @Override
    public void run(String... args) throws Exception {

        String bucketName = config.getBucketName();
        List<Bucket> buckets = client.listBuckets();
        List<String> bucketNames = buckets.stream().map(Bucket::getName).collect(Collectors.toList());
        boolean bucketExist = bucketNames.contains(bucketName);
        // boolean bucketExist = client.doesBucketExist(config.getBucketName());

        if (!bucketExist && config.getAutoCreateBucket()) {
            client.createBucket(config.getBucketName());
        }

        if (!bucketExist && !config.getAutoCreateBucket()) {
            throw new FrameworkException("the bucket " + config.getBucketName() + " is not available");
        }

        log.info("the bucket " + config.getBucketName() + " have been created!");
    }
}
