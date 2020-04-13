package com.example.study.es;

import com.example.study.es.service.EsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EsApplicationTests {

    @Autowired
    private EsService esService;

    @Test
    void contextLoads() {
        System.out.println("spring基于id的增删改查");
        esService.base();
        System.out.println("全文检索");
        esService.fullTextQuery();
        System.out.println("term 查询");
        esService.termLevelQuery();
        System.out.println("布尔逻辑查询");
        esService.boolQuery();
        System.out.println("折叠查询");
        esService.collapse();
        System.out.println("高亮");
        esService.highlight();
        System.out.println("查询建议以及纠错");
        esService.suggest();
        System.out.println("指标聚合");
        esService.metricsAggregations();
        System.out.println("桶聚合");
        esService.bulkAggregations();
    }
}
