package com.cold.blade.bootstrap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:elasticsearch.properties")
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchConfig {

    private String clusterName;
    private String host;
    private int port;
}
