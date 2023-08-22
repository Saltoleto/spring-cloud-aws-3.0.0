package com.saltoleto.s3.service;

import reactor.core.publisher.Mono;

public interface S3Service {
    Mono<Void> uploadObject(String bucketName, String key, byte[] data);
}
