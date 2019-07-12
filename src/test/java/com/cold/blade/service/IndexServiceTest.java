package com.cold.blade.service;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cold.blade.BaseTest;
import com.cold.blade.indexes.Indexes;

/**
 * @version 1.0
 */
public class IndexServiceTest extends BaseTest {

    @Autowired
    private IndexService indexService;

    private CreateIndexResponse response;

    @Override
    public void setUp() {
        super.setUp();
        response = indexService.createIndex(Indexes.EMPLOYEE);
    }

    @Test
    public void testCreateIndex() {
        Assert.assertTrue(response.isShardsAcked());
        Assert.assertEquals(Indexes.EMPLOYEE, response.index());
    }

    @Override
    public void tearDown() {
        indexService.deleteIndex(Indexes.EMPLOYEE);
        super.tearDown();
    }
}
