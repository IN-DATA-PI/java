package dados.s3;

import conexao.banco.Roubos;
import conexao.banco.LeitorExcel;
import noticacoes.slack.NotificacaoDelegado;
import noticacoes.slack.NotificacaoPolicia;
import noticacoes.slack.Slack;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.core.sync.ResponseTransformer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        LocalDate dataAtual = LocalDate.now();
        String dataFormatada = dataAtual.format(DateTimeFormatter.ofPattern("MM.yyyy"));

        // Redireciona a saída do console para o arquivo log.txt
        try (PrintStream logStream = new PrintStream(new FileOutputStream(dataFormatada, true))){
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


            LeitorExcel novaQtd = new LeitorExcel();


            Slack slack = new Slack();
            NotificacaoPolicia policia = new NotificacaoPolicia();
            NotificacaoDelegado delegado = new NotificacaoDelegado();

            slack.enviarNotificacao("Alerta geral!");
            policia.enviarNotificacao("Ocorrência policial registrada.");
            delegado.enviarNotificacao("Investigação em andamento \n" +
                    "Relatório mensal: Foram baixados um total de " +
                     " arquivos \n" +
                    "https://github.com/Reynald-Costa");

        } catch (IOException e) {
            System.err.println("Erro ao criar log.txt: " + e.getMessage());
        }
    }


    public static String getDataHoraAtual(){
        LocalDateTime dataHoraAtual = LocalDateTime.now();
        String dataHoraFormat = dataHoraAtual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        return dataHoraFormat;
    }

    public static void processarBucket(S3Client s3Client, String bucketName, boolean usarAnoFixo) {
        try {
            List<S3Object> objects = s3Client.listObjects(ListObjectsRequest.builder().bucket(bucketName).build()).contents();
            System.out.println("\n" + getDataHoraAtual() + " - Baixando arquivos do bucket " + bucketName + ":");

            List<File> arquivos = new ArrayList<File>();
            for (S3Object object : objects) {
                System.out.println(getDataHoraAtual() + " - Baixando arquivo: " + object.key());

                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(object.key())
                        .build();

                InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
                File file = new File(object.key());
                Files.copy(inputStream, Paths.get("./arquivos", object.key()), StandardCopyOption.REPLACE_EXISTING);
                arquivos.add(file);

                // Chama o método para processar o arquivo Excel
                // Passa o flag usarAnoFixo

                inputStream.close();

                System.out.println(getDataHoraAtual() + " - Arquivo processado com sucesso");

//                if (file.delete()) {
//                    System.out.println("Arquivo " + object.key() + " deletado com sucesso.");
//                } else {
//                    System.err.println("Erro ao tentar deletar o arquivo " + object.key());
//                }
            }
            for(File arquivo : arquivos){
                processarArquivo(arquivo, usarAnoFixo);
            }
        } catch (IOException | S3Exception e) {
            System.err.println("Erro ao baixar e processar arquivos: " + e.getMessage());
        }
    }

    public static void processarArquivo(File arquivo, boolean usarAnoFixo) {
        try {
            InputStream inputStream =  new FileInputStream("./arquivos/" + arquivo.getName());
            String nomeArquivo = arquivo.getName();

            LeitorExcel leitorExcel = new LeitorExcel();
            List<Roubos> roubosExtraidos = leitorExcel.extrairDados(nomeArquivo, inputStream, usarAnoFixo); // Passa o flag usarAnoFixo

            inputStream.close();

            //System.out.println("conexao.banco.Roubos extraídos do arquivo " + nomeArquivo + ":");
//            for (Roubos dado : roubosExtraidos) {
//                System.out.println(dado);
//            }

        } catch (IOException e) {
            System.err.println("Erro ao processar o arquivo " + arquivo.getName() + ": " + e.getMessage());
        }
    }
}
