package com.whf.pan.swagger2;

import com.whf.pan.core.constants.Constants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author whf
 * @className Swagger2ConfigProperties
 * @description TODO
 * @date 2023/10/18 16:58
 */
@Data
@Component
@ConfigurationProperties(prefix = "swagger2")
public class Swagger2ConfigProperties {
    private boolean show = true;

    private String groupName = "r-pan";

    private String basePackage = Constants.BASE_COMPONENT_SCAN_PATH;

    private String title = "server";

    private String description = "server";

    private String termsOfServiceUrl = "http://127.0.0.1:${server.port}";

    private String contactName = "whf";

    private String contactUrl = "https://github.com/Hqk-rgb";

    private String contactEmail = "whfplus7@163.com";

    private String version = "1.0";
}
