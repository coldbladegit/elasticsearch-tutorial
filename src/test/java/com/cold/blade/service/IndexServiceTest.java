package com.cold.blade.service;

import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.index.IndexResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.cold.blade.BaseTest;
import com.cold.blade.indexes.IndexType;
import com.cold.blade.indexes.Indexes;
import com.cold.blade.indexes.Programmer;

/**
 * @version 1.0
 */
public class IndexServiceTest extends BaseTest {

    @Autowired
    private IndexService indexService;

    @Test
    public void testCreateIndex() {
        Programmer p = Programmer.builder()
            .companyName("alibaba")
            .name("cold_blade")
            .age(31)
            .level(7)
            .salary(20000)
            .build();
        IndexResponse response = indexService.createIndex(Indexes.COMPANY, JSON.toJSONString(p), IndexType.PROGRAMMER);

        Assert.assertEquals(Result.CREATED, response.getResult());
        Assert.assertEquals(Indexes.EMPLOYEE, response.getIndex());
        Assert.assertEquals(IndexType.PROGRAMMER.name(), response.getType());
    }
}
