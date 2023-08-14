package com.saltoleto.s3.service;

import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;

class S3TemplateServiceTest {

    @Mock
    private S3Template mockS3Template;

    private S3TemplateService s3TemplateService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        s3TemplateService = new S3TemplateService(mockS3Template);
    }

    @Test
    public void testReadFileFromS3WithS3Template() {
        String bucketName = "test-bucket";
        String objectKey = "test-object";

        s3TemplateService.readFileFromS3WithS3Template(bucketName, objectKey);

        verify(mockS3Template).download(bucketName, objectKey);
    }

    @Test
    public void testUploadObjectWithS3Template() {
        String bucketName = "test-bucket";
        String key = "test-key";
        Map<String, String> body = new HashMap<>();
        body.put("key1", "value1");

        s3TemplateService.uploadObjectWithS3Template(bucketName, key, body);

        verify(mockS3Template).store(bucketName, key, body);
    }

    @Test
    public void testDeleteObjectWithS3Template() {
        String bucketName = "test-bucket";
        String key = "test-key";

        s3TemplateService.deleteObjectWithS3Template(bucketName, key);

        verify(mockS3Template).deleteObject(bucketName, key);
    }

    @Test
    public void testCreateBucketWithS3Template() {
        String bucketName = "test-bucket";

        s3TemplateService.createBucketWithS3Template(bucketName);

        verify(mockS3Template).createBucket(bucketName);
    }
}