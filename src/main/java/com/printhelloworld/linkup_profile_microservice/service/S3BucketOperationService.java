package com.printhelloworld.linkup_profile_microservice.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

@Service
public class S3BucketOperationService {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.region}")
    private String awsRegion;

    public S3BucketOperationService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadObject(File object, String userId) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("company", "PrintHelloWorld");
        metadata.put("environment", "development");

        s3Client.putObject(request -> request
                .bucket(bucketName)
                .key(userId)
                .metadata(metadata),
                // .ifNoneMatch("*"), // Add if overriding is unexpected
                object.toPath());

        return getPublicUrl(userId);
    }

    public void deleteObject(String userId) {
        s3Client.deleteObject(request -> request
                .bucket(bucketName)
                .key(userId));
    }

    public void deleteMultipleObjects(List<String> objectKeys) {
        List<ObjectIdentifier> objectsToDelete = objectKeys
                .stream()
                .map(key -> ObjectIdentifier
                        .builder()
                        .key(key)
                        .build())
                .toList();

        s3Client.deleteObjects(request -> request
                .bucket(bucketName)
                .delete(deleteRequest -> deleteRequest
                        .objects(objectsToDelete)));
    }

    public String getPublicUrl(String objectKey) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, awsRegion, objectKey);
    }

}
