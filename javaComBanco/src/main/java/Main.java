import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.core.sync.ResponseTransformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Redireciona a saída do console para o arquivo log.txt
        try (PrintStream logStream = new PrintStream(new FileOutputStream("log.txt", true))) {
            System.setOut(logStream);
            System.setErr(logStream);

            S3Provider s3Prov = new S3Provider();
            S3Client s3Client = s3Prov.getS3Client();

            // *******************************
            // *   Primeiro Bucket            *
            // *******************************
            String bucketName1 = "s3-lab-reynald";
            processarBucket(s3Client, bucketName1, false);

            // *******************************
            // *   Segundo Bucket             *
            // *******************************
            String bucketName2 = "s3-insigna-2023";
            processarBucket(s3Client, bucketName2, true);

        } catch (IOException e) {
            System.err.println("Erro ao criar log.txt: " + e.getMessage());
        }
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

                // Chama o método para processar o arquivo Excel
                processarArquivo(file, usarAnoFixo); // Passa o flag usarAnoFixo

                inputStream.close();

                System.out.println("Arquivo processado com sucesso: " + object.key());

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

    public static void processarArquivo(File arquivo, boolean usarAnoFixo) {
        try {
            InputStream inputStream = Files.newInputStream(arquivo.toPath());
            String nomeArquivo = arquivo.getName();

            LeitorExcel leitorExcel = new LeitorExcel();
            List<Roubos> roubosExtraidos = leitorExcel.extrairDados(nomeArquivo, inputStream, usarAnoFixo); // Passa o flag usarAnoFixo

            inputStream.close();

            System.out.println("Roubos extraídos do arquivo " + nomeArquivo + ":");
            for (Roubos dado : roubosExtraidos) {
                System.out.println(dado);
            }

        } catch (IOException e) {
            System.err.println("Erro ao processar o arquivo " + arquivo.getName() + ": " + e.getMessage());
        }
    }
}
