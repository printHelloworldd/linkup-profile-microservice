package com.printhelloworld.linkup_profile_microservice.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printhelloworld.linkup_profile_microservice.entity.User;

public interface ProfileJpaRepository extends JpaRepository<User, String> {

}
