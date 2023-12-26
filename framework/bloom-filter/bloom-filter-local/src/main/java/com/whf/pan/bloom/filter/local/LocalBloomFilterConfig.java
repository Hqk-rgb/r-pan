package com.whf.pan.bloom.filter.local;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author whf
 * @className LocalBloomFilterConfig
 * @description TODO
 * @date 2023/12/26 21:11
 */
@Component
@ConfigurationProperties(prefix = "com.whf.pan.bloom.filter.local")
@Data
public class LocalBloomFilterConfig {

    private List<LocalBloomFilterConfigItem> items;
}
