package peakspace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;

@SpringBootApplication
public class PeakSpaceB12Application {

    public static void main(String[] args) {
        SpringApplication.run(PeakSpaceB12Application.class, args);
        String bucketName = "peakspacebucket";
        String keyName = "AKIAZI2LDVKOR6V4MDP7";
        String filePath = "path/to/your/file.txt";
        String secretKey = "q8YvKp5zotyi27B63H+0ZQYPPKFFuDOO06lcvuRD";

        S3Client s3 = S3Client.builder()
                .region(Region.US_EAST_1) // Укажите регион, в котором находится ваш бакет
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            s3.putObject(request, RequestBody.fromFile(new File(filePath)));

            System.out.println("File uploaded successfully.");
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

    }

}
