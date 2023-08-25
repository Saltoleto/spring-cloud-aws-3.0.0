import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.nio.ByteBuffer;

@Service
public class S3Service2 {

    private final S3AsyncClient s3AsyncClient;
    private final String bucketName;

    public S3Service(S3AsyncClient s3AsyncClient, @Value("${s3.bucket}") String bucketName) {
        this.s3AsyncClient = s3AsyncClient;
        this.bucketName = bucketName;
    }

    public Mono<String> uploadObjectToS3(String key, ByteBuffer data) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return Mono.fromFuture(() -> s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromByteBuffer(data)))
                .map(response -> "Object uploaded successfully to S3: " + response);
    }
}
