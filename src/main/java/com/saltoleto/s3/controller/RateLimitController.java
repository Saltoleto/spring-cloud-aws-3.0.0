package com.saltoleto.s3.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@RestController
@RequestMapping("/rates")
public class RateLimitController {

    @GetMapping("{numero}")
    public ResponseEntity<String> readFileFromS3(@PathVariable String numero) {
        System.out.println("Chamada realizada as: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " para o numero " + numero);
        return ResponseEntity.ok().body(numero);
    }

}
