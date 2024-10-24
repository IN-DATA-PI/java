import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class teste {
    public static void main(String[] args) {
        S3Provider s3Prov = new S3Provider();
        S3Client s3Client = s3Prov.getS3Client();

        // *******************************
        // *   Primeiro Bucket            *
        // *******************************
        String bucketName = "s3-insigna";
        processarBucket(s3Client, bucketName, false);

        // *******************************
        // *   Segundo Bucket             *
        // *******************************
        String segundoBucketName = "s3-insigna-2023"; // Altere para o nome correto do segundo bucket
        processarBucket(s3Client, segundoBucketName, true);
    }

    public static void processarBucket(S3Client s3Client, String bucketName, boolean usarAnoFixo) {
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

                processarArquivo(file, usarAnoFixo); // Passa o flag para usarAnoFixo

                System.out.println("Arquivo processado com sucesso: " + object.key());
            }
        } catch (IOException | S3Exception e) {
            System.err.println("Erro ao baixar e processar arquivos: " + e.getMessage());
        }
    }

    // *************************************
    // *   Processando cada arquivo Excel  *
    // *************************************
    public static void processarArquivo(File arquivo, boolean usarAnoFixo) {
        try {
            InputStream inputStream = Files.newInputStream(arquivo.toPath());
            String nomeArquivo = arquivo.getName();

            LeitorExcel leitorExcel = new LeitorExcel();
            List<Dados> dadosExtraidos = leitorExcel.extrairDados(nomeArquivo, inputStream, usarAnoFixo); // Passa o flag

            inputStream.close();

            System.out.println("Dados extraídos do arquivo " + nomeArquivo + ":");
            for (Dados dado : dadosExtraidos) {
                System.out.println(dado);
            }

            if (arquivo.delete()) {
                System.out.println("Arquivo " + nomeArquivo + " deletado com sucesso.");
            } else {
                System.err.println("Erro ao tentar deletar o arquivo " + nomeArquivo);
            }

        } catch (IOException e) {
            System.err.println("Erro ao processar o arquivo " + arquivo.getName() + ": " + e.getMessage());
        }
    }
}
