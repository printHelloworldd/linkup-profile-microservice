package com.printhelloworld.linkup_profile_microservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.printhelloworld.linkup_profile_microservice.entity.User;
import com.printhelloworld.linkup_profile_microservice.entity.UserDocument;
import com.printhelloworld.linkup_profile_microservice.entity.UserDto;
import com.printhelloworld.linkup_profile_microservice.service.ElasticsearchService;
import com.printhelloworld.linkup_profile_microservice.service.ProfileService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.io.File;
import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController("profileRestController")
@RequestMapping("api/v1/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final ElasticsearchService elasticsearchService;
    private final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final Bucket bucket;

    public ProfileController(ProfileService profileService, ElasticsearchService elasticsearchService) {
        this.profileService = profileService;
        this.elasticsearchService = elasticsearchService;
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestPart("data") UserDto userDto,
            @RequestPart("avatar") MultipartFile file) {
        if (bucket.tryConsume(1)) {
            try {
                if (file != null) {
                    File tempFile = File.createTempFile("avatar-", file.getOriginalFilename());
                    file.transferTo(tempFile);

                    userDto.setProfileImage(tempFile);
                }

                final User savedUser = profileService.saveProfile(null, userDto);

                return ResponseEntity.ok(savedUser);
            } catch (Exception e) {
                logger.error("Failed to create a user", e);
                e.printStackTrace();

                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> update(@PathVariable String id, @RequestPart("data") String data,
            @RequestPart("avatar") MultipartFile file) {
        if (bucket.tryConsume(1)) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                UserDto userDto = mapper.readValue(data, UserDto.class);

                if (file != null) {
                    File tempFile = File.createTempFile("avatar-", file.getOriginalFilename());
                    file.transferTo(tempFile);

                    userDto.setProfileImage(tempFile);
                }

                final User updatedUser = profileService.saveProfile(id, userDto);

                return ResponseEntity.ok(updatedUser);
            } catch (Exception e) {
                logger.error("Failed to update user", e);

                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> delete(@PathVariable String id) {
        if (bucket.tryConsume(1)) {
            try {
                profileService.deleteProfile(id);

                return ResponseEntity.ok().build();
            } catch (Exception e) {
                logger.error("Failed to delete user with id: {}", id, e);

                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }

    @GetMapping("/find/{query}")
    public ResponseEntity<List<UserDocument>> findUser(@PathVariable String query) {
        if (bucket.tryConsume(1)) {
            try {
                List<UserDocument> users = elasticsearchService.searchUsers(query);
                users.forEach(u -> logger.debug(u.getUsername() + " | " + u.getDisplayName()));

                return ResponseEntity.ok(users);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Failed to find user with query: {}", query, e);

                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }

    @PutMapping("/block-unblock-user/{id}/{blockedId}")
    public ResponseEntity<Void> blockOrUnblockUser(@PathVariable String id, @PathVariable String blockedId) {
        if (bucket.tryConsume(1)) {
            profileService.blockOrUnblocUser(id, blockedId);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }

}
