package com.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import static org.mockito.Mockito.*;

class S3ReactiveServiceTest {

    private S3AsyncClient s3AsyncClient;
    private S3ReactiveService s3ReactiveService;

    @BeforeEach
    void setUp() {
        s3AsyncClient = mock(S3AsyncClient.class);
        s3ReactiveService = new S3ReactiveService(s3AsyncClient);
    }

    @Test
    void uploadObjectToBucket_ShouldUploadObjectAsync() {
        byte[] testData = "Test data".getBytes();
        PutObjectRequest expectedRequest = PutObjectRequest.builder()
            .bucket("your_bucket_name")
            .key("object_key")
            .build();
        AsyncRequestBody requestBody = AsyncRequestBody.fromBytes(testData);

        when(s3AsyncClient.putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class)))
            .thenReturn(Mono.empty());

        s3ReactiveService.uploadObjectToBucket("your_bucket_name", "object_key", testData).block();

        verify(s3AsyncClient).putObject(expectedRequest, requestBody);
    }
}
