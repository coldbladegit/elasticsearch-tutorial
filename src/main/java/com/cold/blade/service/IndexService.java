package com.cold.blade.service;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cold.blade.bootstrap.ClientSetting;
import com.cold.blade.indexes.IndexType;

@Service
public class IndexService {

    @Autowired
    private ClientSetting setting;

    public IndexResponse createIndex(String index, String jsonObject, IndexType indexType) {
        return createIndex(index, jsonObject, indexType, null);
    }

    public IndexResponse createIndex(String index, String jsonObject, IndexType indexType, @Nullable String id) {
        try (TransportClient client = new PreBuiltTransportClient(setting.getSettings())
            .addTransportAddress(setting.getAddress())) {
            return client.prepareIndex(index, indexType.name(), id)
                .setSource(jsonObject, XContentType.JSON).get();
        }
    }
}
