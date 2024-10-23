import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.core.sync.ResponseTransformer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        S3Provider s3Prov = new S3Provider();
        S3Client s3Client = s3Prov.getS3Client();
        String bucketName1 = "s3-insigna";
        String bucketName2 = "s3-insigna-2023"; // Nome do segundo bucket

        // Processar primeiro bucket
        processarBucket(s3Client, bucketName1, false);

        // Processar segundo bucket
        processarBucket(s3Client, bucketName2, true);
    }

    public static void processarBucket(S3Client s3Client, String bucketName, boolean anoFixo) {
        try {
            List<S3Object> objects = s3Client.listObjects(ListObjectsRequest.builder().bucket(bucketName).build()).contents();
            System.out.println("Baixando arquivos do bucket " + bucketName + ":");

            for (S3Object object : objects) {
                System.out.println("Baixando arquivo: " + object.key());

                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(object.key())
                        .build();

                InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
                File file = new File(object.key());
                Files.copy(inputStream, file.toPath());

                // Chamar extrairDados com o parâmetro anoFixo
                LeitorExcel leitorExcel = new LeitorExcel();
                List<Dados> dadosExtraidos = leitorExcel.extrairDados(object.key(), inputStream, anoFixo);

                inputStream.close();

                System.out.println("Dados extraídos do arquivo " + object.key() + ":");
                for (Dados dado : dadosExtraidos) {
                    System.out.println(dado);
                }

                if (file.delete()) {
                    System.out.println("Arquivo " + object.key() + " deletado com sucesso.");
                } else {
                    System.err.println("Erro ao tentar deletar o arquivo " + object.key());
                }
            }
        } catch (IOException | S3Exception e) {
            System.err.println("Erro ao baixar e processar arquivos: " + e.getMessage());
        }
    }
}
