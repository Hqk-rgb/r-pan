package com.whf.pan.server.common.config;

import com.whf.pan.core.constants.Constants;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author whf
 * @className ServerConfig
 * @description TODO
 * @date 2023/11/11 15:11
 */

@Component
@ConfigurationProperties(prefix = "com.whf.pan.server")
@Data
public class ServerConfig {

    @Value("${server.port}")
    private Integer serverPort;

    /**
     * 文件分片的过期天数
     */
    private Integer chunkFileExpirationDays = Constants.ONE_INT;

    /**
     * 分享链接的前缀
     */
    // private String sharePrefix = "http://127.0.0.1:8080/share/";
    private String sharePrefix = "http://127.0.0.1:"+ serverPort +"/share/";

}
