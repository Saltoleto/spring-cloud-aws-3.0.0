package com.saltoleto.s3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class S3ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(S3ClientApplication.class, args);
	}

}
