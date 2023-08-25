package com.example.demo.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3ReactiveService {

    private final S3AsyncClient s3AsyncClient;

    public S3ReactiveService(S3AsyncClient s3AsyncClient) {
        this.s3AsyncClient = s3AsyncClient;
    }

    public Mono<Void> uploadObjectToBucket(String bucketName, String objectKey, byte[] data) {
        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build();

        AsyncRequestBody requestBody = AsyncRequestBody.fromBytes(data);

        return Mono.fromFuture(() -> s3AsyncClient.putObject(request, requestBody)).then();
    }
}
