import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import java.nio.ByteBuffer;

import static org.mockito.Mockito.*;

public class S3ServiceTest2 {

    @Mock
    private S3AsyncClient s3AsyncClient;

    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        s3Service = new S3Service(s3AsyncClient, "test-bucket");
    }

    @Test
    void testUploadObjectToS3() {
        ByteBuffer data = ByteBuffer.wrap("test-data".getBytes());
        PutObjectRequest expectedRequest = PutObjectRequest.builder()
                .bucket("test-bucket")
                .key("test-key")
                .build();

        PutObjectResponse putObjectResponse = PutObjectResponse.builder()
                .eTag("test-etag")
                .build();

        when(s3AsyncClient.putObject(any(PutObjectRequest.class), any(AsyncRequestBody.class)))
                .thenReturn(Mono.just(putObjectResponse));

        StepVerifier.create(s3Service.uploadObjectToS3("test-key", data))
                .expectNext("Object uploaded successfully to S3: PutObjectResponse{ETag='test-etag'}")
                .verifyComplete();

        ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3AsyncClient, times(1)).putObject(requestCaptor.capture(), any(AsyncRequestBody.class));

        PutObjectRequest capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest).usingRecursiveComparison().isEqualTo(expectedRequest);
    }
}
