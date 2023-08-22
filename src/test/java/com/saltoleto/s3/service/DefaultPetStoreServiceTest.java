package com.saltoleto.s3.service;

import com.saltoleto.s3.model.Pet;
import com.saltoleto.s3.model.STSTokenResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class DefaultPetStoreServiceTest {

    private DefaultPetStoreService petStoreService;

    @Mock
    private STSTokenService stsTokenService;

    @Mock
    private S3Service s3Service;

    private MockWebServer mockWebServer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        petStoreService = new DefaultPetStoreService(stsTokenService, s3Service);

        mockWebServer = new MockWebServer();
        // Start the server
        mockWebServer.start();
    }

    @Test
    public void testGetAllPets() throws InterruptedException {
        // Mocking STS token service
        when(stsTokenService.getSTSToken()).thenReturn(Mono.just(new STSTokenResponse()));

        // Mocking S3 service upload
        when(s3Service.uploadObject(anyString(), anyString(), any(byte[].class))).thenReturn(Mono.empty());

        // Set up the mock API response
        mockWebServer.enqueue(new MockResponse().setBody("[{\"id\": 1, \"name\": \"Dog\"}, {\"id\": 2, \"name\": \"Cat\"}]"));

        // Configure the WebClient base URL to the mock server
        WebClient webClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();
        petStoreService.setWebClient(webClient);

        // Test the method
        Flux<Pet> result = petStoreService.getAllPets();

        // Verify the behavior using StepVerifier
        StepVerifier.create(result)
                .expectNextCount(2) // Expect two pets in the stream
                .verifyComplete();

        // Verify interactions with the mocks
        verify(stsTokenService).getSTSToken();
        verify(s3Service, times(2)).uploadObject(anyString(), anyString(), any(byte[].class));

        // Verify API request
        assertThat(mockWebServer.getRequestCount()).isEqualTo(1);

        // Shut down the server
        mockWebServer.shutdown();
    }
}
