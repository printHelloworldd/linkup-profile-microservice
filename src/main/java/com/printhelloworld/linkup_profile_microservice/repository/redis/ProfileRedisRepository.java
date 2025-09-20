package com.printhelloworld.linkup_profile_microservice.repository.redis;

import org.springframework.data.repository.CrudRepository;

import com.printhelloworld.linkup_profile_microservice.entity.User;

public interface ProfileRedisRepository extends CrudRepository<User, String> {

}
