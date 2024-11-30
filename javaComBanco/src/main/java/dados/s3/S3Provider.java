package dados.s3;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class S3Provider {

    private final AwsSessionCredentials credentials;
    static S3Client s3Client = new S3Provider().getS3Client();

    public S3Provider() {
        this.credentials = AwsSessionCredentials.create(
                System.getenv("aws_access_key_id"),
                System.getenv("aws_secret_access_key"),
                System.getenv("aws_session_token")
        );
    }

    public S3Client getS3Client() {
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(() -> credentials)
                .build();
    }

    public void uploadFile(String bucketName, String key, String filePath) {
        S3Client s3Client = getS3Client();
        try {
            // Cria a requisição para enviar o arquivo
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // Envia o arquivo para o bucket
            PutObjectResponse response = s3Client.putObject(putObjectRequest, Paths.get(filePath));

            System.out.println("Upload realizado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao enviar arquivo para o S3!");
        } finally {
            s3Client.close(); // Libera recursos
        }
    }
}