package com.cold.blade.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cold.blade.BaseTest;

/**
 * @version 1.0
 */
public class ClusterServiceTest extends BaseTest {

    @Autowired
    private ClusterService clusterService;

    @Test
    public void testCloseAutoCreateIndex() {
        Assert.assertFalse(clusterService.closeAutoCreateIndexFunction());
    }
}
