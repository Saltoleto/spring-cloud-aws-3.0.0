import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.ByteBuffer;

@Service
public class ObjectStorageService {

    private final S3AsyncClient s3AsyncClient;
    private final WebClient webClient;

    @Value("${your.bucket.name}")
    private String bucketName;

    public ObjectStorageService(S3AsyncClient s3AsyncClient, WebClient.Builder webClientBuilder) {
        this.s3AsyncClient = s3AsyncClient;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public Mono<Void> uploadObjectAsync(String fileKey, ByteBuffer fileContent) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();

        return s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromByteBuffer(fileContent))
                .then(Mono.empty());
    }

    public Mono<Void> uploadObjectFromRemote(String fileKey, String remoteUrl) {
        return webClient.get()
                .uri(remoteUrl)
                .exchangeToMono(response -> response.bodyToMono(ByteBuffer.class))
                .flatMap(byteBuffer -> uploadObjectAsync(fileKey, byteBuffer));
    }
}
