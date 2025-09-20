package com.printhelloworld.linkup_profile_microservice.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.printhelloworld.linkup_profile_microservice.repository.elasticsearch")
public class ElasticsearchConfig {
    public ElasticsearchClient createClient() {
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200)).build();
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper(new ObjectMapper()));
        return new ElasticsearchClient(transport);
    }
}
