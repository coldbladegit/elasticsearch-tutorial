package com.cold.blade.service;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cold.blade.bootstrap.ClientSetting;

@Service
public class IndexService {

    @Autowired
    private ClientSetting setting;

    public CreateIndexResponse createIndex(String index) {
        try (TransportClient client = new PreBuiltTransportClient(setting.getSettings())
            .addTransportAddress(setting.getAddress())) {
            return client.admin().indices().prepareCreate(index).get();
        }
    }

    public DeleteIndexResponse deleteIndex(String... index) {
        try (TransportClient client = new PreBuiltTransportClient(setting.getSettings())
            .addTransportAddress(setting.getAddress())) {
            return client.admin().indices().prepareDelete(index).get();
        }
    }

    public UpdateSettingsResponse updateSettings(Settings settings, String... index) {
        UpdateSettingsRequest request = new UpdateSettingsRequest(settings, index);
        try (TransportClient client = new PreBuiltTransportClient(setting.getSettings())
            .addTransportAddress(setting.getAddress())) {
            return client.admin().indices().updateSettings(request).actionGet();
        }
    }
}
