package com.printhelloworld.linkup_profile_microservice.entity;

import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.RedisHash;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

@Data
@RedisHash("User")
@Entity(name = "User")
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_id", columnNames = "id"),
// @UniqueConstraint(name = "unique_username", columnNames = "username")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false, updatable = false)
    private String id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "display_name", nullable = false)
    private String displayName;
    @Column(name = "age", nullable = true)
    private int age;
    @Column(name = "gender", nullable = true)
    private String gender;
    @Column(name = "bio", nullable = true)
    private String bio;
    @ElementCollection
    @CollectionTable(name = "user_blocked_users", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "blocked_users", nullable = false)
    private List<String> blockedUsers;
    @Column(name = "city", nullable = true)
    private String city;
    @Column(name = "country", nullable = true)
    private String country;
    @Column(name = "profile_image_url", nullable = true)
    private String profileImageUrl;
    @ElementCollection
    @CollectionTable(name = "user_social_media_links", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "platform")
    @Column(name = "link", nullable = false)
    private Map<String, String> socialMediaLinks;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hobby> hobbies;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restricted_data_id")
    private RestrictedUserData restrictedData;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "private_data_id")
    private PrivateUserData privateData;

    public User() {
    }

    public User(String id, String username, String email, String displayName, int age, String gender, String bio,
            List<String> blockedUsers, String city, String country, String profileImageUrl,
            Map<String, String> socialMediaLinks, List<Hobby> hobbies, RestrictedUserData restrictedData,
            PrivateUserData privateData) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.displayName = displayName;
        this.age = age;
        this.gender = gender;
        this.bio = bio;
        this.blockedUsers = blockedUsers;
        this.city = city;
        this.country = country;
        this.profileImageUrl = profileImageUrl;
        this.socialMediaLinks = socialMediaLinks;
        this.hobbies = hobbies;
        this.restrictedData = restrictedData;
        this.privateData = privateData;
    }

    public static User toEntity(UserDto userDto, String id, String profileImageUrl) {
        return new User(id, userDto.getUsername(),
                userDto.getEmail(), userDto.getDisplayName(), userDto.getAge(), userDto.getGender(), userDto.getBio(),
                userDto.getBlockedUsers(), userDto.getCity(), userDto.getCountry(),
                profileImageUrl, userDto.getSocialMediaLinks(), userDto.getHobbies(), userDto.getRestrictedData(),
                userDto.getPrivateData());
    }

    public static UserDto toDto(User user) {
        final UserDto userDto = new UserDto();

        userDto.setUsername(user.username);
        userDto.setEmail(user.email);
        userDto.setDisplayName(user.displayName);
        userDto.setAge(user.age);
        userDto.setGender(user.gender);
        userDto.setBio(user.bio);
        userDto.setCity(user.city);
        userDto.setCountry(user.country);
        userDto.setSocialMediaLinks(user.socialMediaLinks);
        userDto.setBlockedUsers(user.blockedUsers);
        userDto.setHobbies(user.hobbies);
        userDto.setRestrictedData(user.restrictedData);
        userDto.setPrivateData(user.privateData);

        return userDto;
    }

}
