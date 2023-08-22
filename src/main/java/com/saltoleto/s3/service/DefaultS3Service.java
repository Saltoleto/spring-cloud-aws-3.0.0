package com.saltoleto.s3.service;

import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class DefaultS3Service implements S3Service {
    private final S3AsyncClient s3AsyncClient;

    @Autowired
    public DefaultS3Service(S3AsyncClient s3AsyncClient) {
        this.s3AsyncClient = s3AsyncClient;
    }

    @Override
    public Mono<Void> uploadObject(String bucketName, String key, byte[] data) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return Mono.fromFuture(() -> s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromBytes(data)))
                .then();
    }
}
