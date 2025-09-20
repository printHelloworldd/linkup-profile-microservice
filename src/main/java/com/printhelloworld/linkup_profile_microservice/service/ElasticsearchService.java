package com.printhelloworld.linkup_profile_microservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.printhelloworld.linkup_profile_microservice.entity.UserDocument;

@Service("elasticsearchService")
public class ElasticsearchService {
    private final ElasticsearchClient client;

    public ElasticsearchService(ElasticsearchClient client) {
        this.client = client;
    }

    @Cacheable(value = "PROFILE_CACHE", key = "#queryText")
    public List<UserDocument> searchUsers(String queryText) throws IOException {
        System.out.println("Getting user from elastic search");
        SearchResponse<UserDocument> response = client.search(s -> s
                .index("users") // индекс в Elasticsearch
                .query(q -> q
                        .multiMatch(m -> m
                                .fields("userId", "username", "displayName") // поля для поиска
                                .query(queryText))),
                UserDocument.class // десериализация результата в объект User
        );

        // Извлекаем список пользователей
        return response.hits().hits().stream()
                .map(Hit::source) // достаем сам объект User
                .filter(Objects::nonNull)
                .toList();
    }

    public void indexDocument(String index, String id, Object document) throws IOException {
        IndexResponse response = client.index(i -> i
                .index(index)
                .id(id)
                .document(document));
        System.out.println("Indexed document with ID: " + response.id());
    }

    public <T> T getDocument(String index, String id, Class<T> clazz) throws IOException {
        GetResponse<T> response = client.get(g -> g
                .index(index)
                .id(id),
                clazz);

        if (response.found()) {
            return response.source();
        } else {
            return null;
        }
    }

    public <T> SearchResponse<T> searchDocuments(String index, Query query, Class<T> clazz) throws IOException {
        return client.search(s -> s
                .index(index)
                .query(query),
                clazz);
    }
}
