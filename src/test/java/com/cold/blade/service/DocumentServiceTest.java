package com.cold.blade.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private ClusterService clusterService;

    private Map<String, Programmer> programmerMap;

    @Override
    public void setUp() {
        super.setUp();
        clusterService.openAutoCreateIndex();

        List<Programmer> programmers = Lists.newArrayList(
            Programmer.builder().name("test_1").age(20).build(),
            Programmer.builder().name("test_2").age(30).build(),
            Programmer.builder().name("test_3").age(40).build()
        );

        programmerMap = programmers.stream()
            .map(p -> {
                IndexResponse response = documentService.createOrUpdateDocument(Indexes.EMPLOYEE, IndexType.PROGRAMMER, JSON.toJSONString(p));
                if (!Strings.isNullOrEmpty(response.getId())) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", response.getId());
                    obj.put("source", p);
                    return obj;
                }
                return null;
            }).filter(Objects::nonNull)
            .collect(Collectors.toMap(e -> e.getString("id"), e -> (Programmer) e.get("source")));
    }

    @Test
    public void testGet() {
        String docId = programmerMap.entrySet().stream().findFirst().map(Entry::getKey).orElse(null);
        Assert.assertNotNull(docId);
        // 传type的情形
        GetResponse response = documentService.getDocument(Indexes.EMPLOYEE, IndexType.PROGRAMMER, docId);
        Assert.assertTrue(response.isExists());

        // 传错误type的情形
        response = documentService.getDocument(Indexes.EMPLOYEE, IndexType.BOSS, docId);
        Assert.assertFalse(response.isExists());

        // 不传type的情形
        response = documentService.getDocument(Indexes.EMPLOYEE, null, docId);
        Assert.assertTrue(response.isExists());
        Programmer cold = new JSONObject(response.getSource()).toJavaObject(Programmer.class);
        Assert.assertEquals(programmerMap.get(docId).getAge(), cold.getAge());
    }

    @Test
    public void testSearch() {
        SearchResponse response = documentService.rangeAge(25, 40);
        List<SearchHit> hits = Arrays.asList(response.getHits().getHits());
        Assert.assertFalse(hits.isEmpty());
        Assert.assertTrue(hits.stream()
            .map(hit -> JSON.parseObject(hit.getSourceAsString(), Programmer.class))
            .noneMatch(p -> p.getAge() < 25 || p.getAge() > 40));
    }

    @Override
    public void tearDown() {
        if (programmerMap.isEmpty()) {
            return;
        }
        programmerMap.forEach((key, value) -> documentService.deleteDocument(Indexes.EMPLOYEE, IndexType.PROGRAMMER, key));
        super.tearDown();
    }
}
