package com.printhelloworld.linkup_profile_microservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "users")
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDocument {
    @Id
    private String userId;

    @Field(type = FieldType.Text)
    private String username;

    @Field(type = FieldType.Text)
    private String email;

    @Field(type = FieldType.Text)
    private String displayName;

    @Field(type = FieldType.Integer)
    private int age;

    @Field(type = FieldType.Text)
    private String gender;

    @Field(type = FieldType.Text)
    private String bio;

    @Field(type = FieldType.Text)
    private String city;

    @Field(type = FieldType.Text)
    private String country;

    public UserDocument(String userId, String username, String email, String displayName, int age, String gender,
            String bio, String city, String country) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.displayName = displayName;
        this.age = age;
        this.gender = gender;
        this.bio = bio;
        this.city = city;
        this.country = country;
    }

}
