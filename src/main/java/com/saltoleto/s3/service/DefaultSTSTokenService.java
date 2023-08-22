package com.saltoleto.s3.service;

import com.saltoleto.s3.model.STSTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;


public class DefaultSTSTokenService implements STSTokenService{
    private final StsClient stsClient;

    @Autowired
    public DefaultSTSTokenService(StsClient stsClient) {
        this.stsClient = stsClient;
    }

    @Override
    public Mono<STSTokenResponse> getSTSToken() {
        return Mono.fromCallable(() -> {
            GetSessionTokenResponse sessionTokenResponse = stsClient.getSessionToken();
            AwsSessionCredentials sessionCredentials = sessionTokenResponse.credentials();

            return new STSTokenResponse(
                    sessionCredentials.accessKeyId(),
                    sessionCredentials.secretAccessKey(),
                    sessionCredentials.sessionToken()
            );
        });
    }
}
