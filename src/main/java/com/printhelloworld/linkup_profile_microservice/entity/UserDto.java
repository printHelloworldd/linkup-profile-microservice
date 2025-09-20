package com.printhelloworld.linkup_profile_microservice.entity;

import java.io.File;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String email;
    private String displayName;
    private int age;
    private String gender;
    private String bio;
    private String city;
    private String country;
    private File profileImage;
    private Map<String, String> socialMediaLinks;
    private List<String> blockedUsers;
    private List<Hobby> hobbies;
    private RestrictedUserData restrictedData;
    private PrivateUserData privateData;
}
