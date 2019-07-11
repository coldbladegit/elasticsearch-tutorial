package com.cold.blade.service;

import java.util.Objects;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cold.blade.bootstrap.ClientSetting;
import com.cold.blade.indexes.IndexType;

@Service
public class DocumentService {

    @Autowired
    private ClientSetting setting;

    public GetResponse getDocument(String index, @Nullable IndexType type, String docId) {
        try (TransportClient client = new PreBuiltTransportClient(setting.getSettings())
            .addTransportAddress(setting.getAddress())) {
            return client.prepareGet(index, Objects.isNull(type) ? null : type.name(), docId).get();
        }
    }

    public DeleteResponse deleteDocument(String index, IndexType type, String docId) {
        try (TransportClient client = new PreBuiltTransportClient(setting.getSettings())
            .addTransportAddress(setting.getAddress())) {
            return client.prepareDelete(index, type.name(), docId).get();
        }
    }

    public SearchResponse rangeAge(int begin, int include) {
        try (TransportClient client = new PreBuiltTransportClient(setting.getSettings())
            .addTransportAddress(setting.getAddress())) {
            return client.prepareSearch()
                // DFS_QUERY_THEN_FETCH 需要对结果进行一个算分的额外操作
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setPostFilter(QueryBuilders.rangeQuery("age").from(begin).to(include))
                .execute()
                .actionGet();
        }
    }
}
