package com.saltoleto.s3.service;

import com.saltoleto.s3.model.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DefaultPetStoreService implements PetStoreService {

    private final WebClient webClient;
    private final STSTokenService stsTokenService;
    private final S3Service s3Service;

    @Autowired
    public DefaultPetStoreService(WebClient.Builder webClientBuilder, STSTokenService stsTokenService, S3Service s3Service) {
        this.webClient = webClientBuilder.baseUrl("https://api.petstore.com").build();
        this.stsTokenService = stsTokenService;
        this.s3Service = s3Service;
    }

    @Override
    public Flux<Pet> getAllPets() {
        return stsTokenService.getSTSToken()
                .flatMapMany(token -> webClient.get()
                        .uri("/pets")
                        .header("Authorization", "Bearer " + token.getSessionToken())
                        .retrieve()
                        .bodyToFlux(Pet.class)
                )
                .flatMap(this::uploadPetToS3); // Upload each pet to S3
    }

    private Mono<Pet> uploadPetToS3(Pet pet) {
        String bucketName = "your-s3-bucket";
        String key = "pets/" + pet.getId() + ".json";
        byte[] jsonData = convertPetToJsonBytes(pet);

        return s3Service.uploadObject(bucketName, key, jsonData)
                .thenReturn(pet);
    }

    private byte[] convertPetToJsonBytes(Pet pet) {
        // Convert the pet object to JSON format and return the byte array
        // You can use libraries like Jackson or Gson for JSON serialization
        return new byte[0];
    }
}
