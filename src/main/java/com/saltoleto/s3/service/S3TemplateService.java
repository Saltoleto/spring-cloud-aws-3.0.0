package com.saltoleto.s3.service;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@AllArgsConstructor
@Service
public class S3TemplateService {

    private final S3Template s3Template;

    public S3Resource readFileFromS3WithS3Template(String bucketName, String objectKey) {
        return s3Template.download(bucketName, objectKey);
    }

    public void uploadObjectWithS3Template(String bucketName, String key, Map<String, String> body) {
        s3Template.store(bucketName, key, body);
    }

    public void deleteObjectWithS3Template(String bucketName, String key) {
        s3Template.deleteObject(bucketName, key);
    }

    public void createBucketWithS3Template(String bucketName) {
        s3Template.createBucket(bucketName);
    }
}
