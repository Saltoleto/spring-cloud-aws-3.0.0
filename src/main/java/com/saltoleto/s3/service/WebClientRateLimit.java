package com.saltoleto.s3.service;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class WebClientRateLimit {
    @Autowired
    private WebClient webClient;
    private final RateLimiter rateLimiter;

    public WebClientRateLimit() {
        this.rateLimiter = RateLimiter.of("my-rate-limiter",
                RateLimiterConfig.custom()
                        .limitRefreshPeriod(Duration.ofSeconds(10))
                        .limitForPeriod(6)
                        .timeoutDuration(Duration.ofMinutes(1))
                        .build());
    }

    public Mono<String> fetchRequest(String url, String numero) {
        return webClient.get()
                .uri(url.concat(numero))
                .retrieve()
                .bodyToMono(String.class)
                .doOnSubscribe(s -> System.out.println("Chamada agendada as: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " para o numero " + numero ))
                .transformDeferred(RateLimiterOperator.of(rateLimiter));
    }

}
