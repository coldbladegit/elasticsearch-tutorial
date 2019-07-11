package com.cold.blade.service;

import java.util.Arrays;
import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.search.SearchHit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cold.blade.BaseTest;
import com.cold.blade.indexes.Boss;
import com.cold.blade.indexes.IndexType;
import com.cold.blade.indexes.Indexes;
import com.cold.blade.indexes.Programmer;
import com.google.common.collect.Lists;

/**
 * @version 1.0
 */
public class DocumentServiceTest extends BaseTest {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private IndexService indexService;

    private List<Programmer> programmers;
    private List<String> docIds;

    @Override
    public void setUp() {
        super.setUp();

        programmers = Lists.newArrayList(
            Programmer.builder().name("test_1").age(20).build(),
            Programmer.builder().name("test_2").age(30).build(),
            Programmer.builder().name("test_3").age(40).build()
        );
        docIds = Lists.newArrayList();

        programmers.forEach(p -> {
            IndexResponse response = indexService.createIndex(Indexes.EMPLOYEE, JSON.toJSONString(p), IndexType.PROGRAMMER);
            if (!Strings.isNullOrEmpty(response.getId())) {
                docIds.add(response.getId());
            }
        });
    }

    @Test
    public void testGet() {
        String docId = "8888";
        // 传type的情形
        GetResponse response = documentService.getDocument(Indexes.EMPLOYEE, IndexType.BOSS, docId);
        Assert.assertTrue(response.isExists());

        // 传错误type的情形
        response = documentService.getDocument(Indexes.EMPLOYEE, IndexType.PROGRAMMER, docId);
        Assert.assertFalse(response.isExists());

        // 不传type的情形
        response = documentService.getDocument(Indexes.EMPLOYEE, null, docId);
        Assert.assertTrue(response.isExists());
        Boss cold = new JSONObject(response.getSource()).toJavaObject(Boss.class);
        Assert.assertEquals(30, cold.getAge());
    }

    @Test
    public void testSearch() {
        SearchResponse response = documentService.rangeAge(35, 40);
        List<SearchHit> hits = Arrays.asList(response.getHits().getHits());
        Assert.assertFalse(hits.isEmpty());
        Assert.assertTrue(hits.stream()
            .map(hit -> JSON.parseObject(hit.getSourceAsString(), Programmer.class))
            .noneMatch(p -> p.getAge() < 35 || p.getAge() > 40));
    }

    @Override
    public void tearDown() {
        docIds.forEach(id -> documentService.deleteDocument(Indexes.EMPLOYEE, IndexType.PROGRAMMER, id));
        super.tearDown();
    }
}
