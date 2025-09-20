package com.printhelloworld.linkup_profile_microservice.repository.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.printhelloworld.linkup_profile_microservice.entity.UserDocument;

public interface ProfileElasticsearchRepository extends ElasticsearchRepository<UserDocument, String> {
    List<UserDocument> findByUserId(String id);
}
