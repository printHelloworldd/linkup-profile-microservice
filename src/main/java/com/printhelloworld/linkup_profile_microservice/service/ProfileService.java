package com.printhelloworld.linkup_profile_microservice.service;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.printhelloworld.linkup_profile_microservice.entity.User;
import com.printhelloworld.linkup_profile_microservice.entity.UserDocument;
import com.printhelloworld.linkup_profile_microservice.entity.UserDto;
import com.printhelloworld.linkup_profile_microservice.exceptions.UserNotFoundException;
import com.printhelloworld.linkup_profile_microservice.repository.elasticsearch.ProfileElasticsearchRepository;
import com.printhelloworld.linkup_profile_microservice.repository.jpa.ProfileJpaRepository;

@Service("profileService")
public class ProfileService {
    private final ProfileJpaRepository profileJpaRepository;
    private final ProfileElasticsearchRepository profileElasticsearchRepository;
    private final S3BucketOperationService s3BucketOperationService;

    public ProfileService(ProfileJpaRepository profileJpaRepository,
            ProfileElasticsearchRepository profileElasticsearchRepository,
            S3BucketOperationService s3BucketOperationService) {
        this.profileJpaRepository = profileJpaRepository;
        this.profileElasticsearchRepository = profileElasticsearchRepository;
        this.s3BucketOperationService = s3BucketOperationService;
    }

    public User saveProfile(String id, UserDto userDto) {
        id = id != null ? id : UUID.randomUUID().toString();

        final File profileImage = userDto.getProfileImage();
        String profileImageUrl = null;
        if (profileImage != null) {
            profileImageUrl = s3BucketOperationService.uploadObject(userDto.getProfileImage(),
                    id);
        } else {
            profileImageUrl = s3BucketOperationService.getPublicUrl(id);
        }

        final User user = User.toEntity(userDto, id, profileImageUrl);

        final UserDocument userDocument = new UserDocument(user.getId(), user.getUsername(), user.getEmail(),
                user.getDisplayName(), user.getAge(), user.getGender(), user.getBio(), user.getCity(),
                user.getCountry());
        profileElasticsearchRepository.save(userDocument);

        return profileJpaRepository.save(user);
    }

    public void deleteProfile(String id) {
        s3BucketOperationService.deleteObject(id);
        profileJpaRepository.deleteById(id);
        profileElasticsearchRepository.deleteById(id);
    }

    public void blockOrUnblocUser(String id, String blockedId) throws UserNotFoundException {
        final Optional<User> storedUser = profileJpaRepository.findById(id);

        if (!storedUser.isPresent()) {
            throw new UserNotFoundException(id);
        }

        if (!profileJpaRepository.findById(blockedId).isPresent()) {
            throw new UserNotFoundException(id);
        }

        final User user = storedUser.get();

        final List<String> blockedUsers = user.getBlockedUsers();
        if (blockedUsers.contains(blockedId)) {
            blockedUsers.remove(blockedId);
        } else {
            blockedUsers.add(blockedId);
        }

        user.setBlockedUsers(blockedUsers);

        saveProfile(id, User.toDto(user));
    }
}
