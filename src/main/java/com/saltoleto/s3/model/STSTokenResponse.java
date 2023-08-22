package com.saltoleto.s3.model;

import lombok.Data;

@Data
public class STSTokenResponse {
    private String sessionToken;
    public String getSessionToken() {
        return sessionToken;
    }
}
