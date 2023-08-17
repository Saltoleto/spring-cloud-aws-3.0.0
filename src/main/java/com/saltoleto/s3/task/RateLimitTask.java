package com.saltoleto.s3.task;

import com.saltoleto.s3.service.WebClientRateLimit;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@AllArgsConstructor
@Component
public class RateLimitTask {

    private final WebClientRateLimit webClientRateLimit;

    @Scheduled(fixedRate = 60)
    public void task() {
        Flux<String> stringFlux = Mono.just(Arrays.asList("UM", "DOIS", "TRES", "QUATRO", "CINCO", "SEIS"))
                .flatMapMany(Flux::fromIterable).log();

        stringFlux.flatMap(numero -> webClientRateLimit.fetchRequest("http://localhost:8080/rates/", numero))
                .blockLast();
    }

}
