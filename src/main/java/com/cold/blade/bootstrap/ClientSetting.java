package com.cold.blade.bootstrap;

import java.net.InetAddress;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public final class ClientSetting implements InitializingBean {

    private InetSocketTransportAddress address;
    private Settings settings;

    @Autowired
    private ElasticSearchConfig config;

    @Override
    public void afterPropertiesSet() throws Exception {
        address = new InetSocketTransportAddress(InetAddress.getByName(config.getHost()), config.getPort());
        settings = Settings.builder()
            .put("client.transport.sniff", true)
            .put("cluster.name", config.getClusterName())
            .build();
    }
}
