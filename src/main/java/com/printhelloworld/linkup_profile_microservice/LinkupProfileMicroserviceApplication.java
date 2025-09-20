package com.printhelloworld.linkup_profile_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LinkupProfileMicroserviceApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(LinkupProfileMicroserviceApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
