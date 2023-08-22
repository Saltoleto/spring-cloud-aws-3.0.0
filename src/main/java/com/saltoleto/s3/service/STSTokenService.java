package com.saltoleto.s3.service;

import com.saltoleto.s3.model.STSTokenResponse;
import reactor.core.publisher.Mono;

public interface STSTokenService {
    Mono<STSTokenResponse> getSTSToken();
}
