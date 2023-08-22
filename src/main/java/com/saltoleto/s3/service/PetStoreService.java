package com.saltoleto.s3.service;

import com.saltoleto.s3.model.Pet;
import reactor.core.publisher.Flux;

public interface PetStoreService {
    Flux<Pet> getAllPets();
}
