package com.whf.pan.bloom.filter.local.test;

import com.whf.pan.bloom.filter.core.BloomFilter;
import com.whf.pan.bloom.filter.local.LocalBloomFilterManager;
import com.whf.pan.core.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author whf
 * @className LocalBloomFilterTest
 * @description TODO
 * @date 2023/12/26 21:18
 */
@SpringBootTest(classes = LocalBloomFilterTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootApplication(scanBasePackages = Constants.BASE_COMPONENT_SCAN_PATH + ".bloom.filter.local")
@Slf4j
public class LocalBloomFilterTest {
    @Autowired
    private LocalBloomFilterManager manager;

    /**
     * 测试本地布隆过滤器
     */
    @Test
    public void localBloomFilterTest() {
        BloomFilter<Integer> bloomFilter = manager.getFilter("test");
        long failNum = 0L;
        for (int i = 0; i < 1000000; i++) {
            bloomFilter.put(i);
        }
        for (int j = 1000000; j < 1100000; j++) {
            boolean result = bloomFilter.mightContain(j);
            if (result) {
                failNum++;
            }
        }
        log.info("test num {}, fail num {}", 100000, failNum);
    }
}
