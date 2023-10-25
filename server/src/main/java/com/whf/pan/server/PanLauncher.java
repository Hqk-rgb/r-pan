package com.whf.pan.server;

import com.whf.pan.core.constants.Constants;
import com.whf.pan.core.response.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

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
@Api("接口测试类")
@Validated
public class PanLauncher {
    public static void main(String[] args) {
        SpringApplication.run(PanLauncher.class);
    }
    @GetMapping("hello")
    public R<String> hello(@NotBlank(message = "name 不能为空") String name) {
        System.out.println(Thread.currentThread().getContextClassLoader());
        return R.success("hello " + name + "! hava changed ~~");
    }
}
