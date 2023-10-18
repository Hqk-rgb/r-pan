package com.whf.pan.server;

import com.whf.pan.core.constants.Constants;
import com.whf.pan.core.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whf
 * @className PanLauncher
 * @description TODO
 * @date 2023/10/18 14:58
 */
@SpringBootApplication(scanBasePackages = Constants.BASE_COMPONENT_SCAN_PATH)
@ServletComponentScan(basePackages = Constants.BASE_COMPONENT_SCAN_PATH)
@RestController
@Slf4j
public class PanLauncher {
    public static void main(String[] args) {
        SpringApplication.run(PanLauncher.class);
    }
    @GetMapping("hello")
    public R hello(String name) {
        return R.data("hello " + name + "!");
    }
}
