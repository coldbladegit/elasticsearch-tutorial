package com.cold.blade.service;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsResponse;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cold.blade.bootstrap.ClientSetting;

@Service
public class ClusterService {

    @Autowired
    private ClientSetting setting;

    public ClusterHealthResponse queryClusterHealth() {
        try (TransportClient client = new PreBuiltTransportClient(setting.getSettings()).addTransportAddress(setting.getAddress())) {
            return client.admin().cluster().prepareHealth().get();
        }
    }

    public ClusterStatsResponse queryClusterStats() {
        try (TransportClient client = new PreBuiltTransportClient(setting.getSettings()).addTransportAddress(setting.getAddress())) {
            return client.admin().cluster().prepareClusterStats().get();
        }
    }

    public ClusterStateResponse queryClusterState() {
        try (TransportClient client = new PreBuiltTransportClient(setting.getSettings()).addTransportAddress(setting.getAddress())) {
            return client.admin().cluster().prepareState().clear().setMetaData(true).setIndices("people").get();
        }
    }

    public boolean closeAutoCreateIndex() {
        return enableAutoCreateIndex(false);
    }

    public boolean openAutoCreateIndex() {
        return enableAutoCreateIndex(true);
    }

    private boolean enableAutoCreateIndex(boolean enable) {
        try (TransportClient client = new PreBuiltTransportClient(setting.getSettings()).addTransportAddress(setting.getAddress())) {
            Settings.Builder autoCreateIndex = Settings.builder().put("action.auto_create_index", enable);
            ClusterUpdateSettingsResponse result = client.admin().cluster().prepareUpdateSettings().setPersistentSettings(autoCreateIndex).get();
            return result.getPersistentSettings().getAsBoolean("action.auto_create_index", !enable);
        }
    }
}
