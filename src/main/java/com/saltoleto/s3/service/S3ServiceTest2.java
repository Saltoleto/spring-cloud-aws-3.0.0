import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.test.StepVerifier;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.nio.ByteBuffer;

public class S3ServiceTest2 {

    private static S3AsyncClient s3AsyncClient;
    private static MockWebServer mockWebServer;
    private static S3Service s3Service;

    @BeforeAll
    static void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        
        String serverUrl = mockWebServer.url("/").toString();
        s3AsyncClient = S3AsyncClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(new URI(serverUrl))
                .build();

        s3Service = new S3Service(s3AsyncClient, "test-bucket");
    }

    @AfterAll
    static void tearDown() throws Exception {
        s3AsyncClient.close();
        mockWebServer.shutdown();
    }

    @Test
    void testUploadObjectToS3() {
        ByteBuffer data = ByteBuffer.wrap("test-data".getBytes());
        PutObjectRequest expectedRequest = PutObjectRequest.builder()
                .bucket("test-bucket")
                .key("test-key")
                .build();

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"ETag\":\"test-etag\"}")
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        SdkPublisher<ByteBuffer> requestBody = AsyncRequestBody.fromPublisher(data);
        
        StepVerifier.create(s3Service.uploadObjectToS3("test-key", data))
                .expectNext("Object uploaded successfully to S3: PutObjectResponse{ETag='test-etag'}")
                .verifyComplete();

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("PUT");
        assertThat(recordedRequest.getPath()).isEqualTo("/test-key");
        assertThat(recordedRequest.getBody().readUtf8()).isEqualTo("test-data");
    }
}
