package conexao.banco;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static dados.s3.Main.getDataHoraAtual;

public class LeitorExcel {

    public List<Roubos> extrairDados(String nomeArquivo, InputStream arquivo, boolean anoFixo) {
        List<Roubos> roubosExtraidos = new ArrayList<>();

        try {
            System.out.println("\n%s - Iniciando leitura do arquivo %s".formatted(getDataHoraAtual(), nomeArquivo));

            Workbook workbook;
            if (nomeArquivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(arquivo);
            } else {
                workbook = new HSSFWorkbook(arquivo);
            }

            Sheet sheet = workbook.getSheetAt(0);

            InsignaConexao conexao = new InsignaConexao();
            JdbcTemplate con = conexao.getConexaoComBanco();

            String dp = extrairDP(nomeArquivo);
            Integer ano;
            if (anoFixo) {
                ano = 2023; // Ano fixo para o segundo bucket
            } else {
                ano = extrairAno(nomeArquivo); // Extrair o ano do nome do arquivo
            }

            Integer qtdInseridos = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                String palavraProcurada = "Roubo";
                String valorCelula = row.getCell(0).getStringCellValue().toLowerCase();
                String natureza = row.getCell(0).getStringCellValue();

                if (valorCelula.contains(palavraProcurada.toLowerCase())) {
                    Roubos roubos = new Roubos();
                    roubos.setDp(dp);
                    roubos.setAno(ano);
                    roubos.setNatureza(natureza);

                    roubos.setJaneiro(converterParaIntegerOuZero(row.getCell(1).getStringCellValue()));
                    roubos.setFevereiro(converterParaIntegerOuZero(row.getCell(2).getStringCellValue()));
                    roubos.setMarco(converterParaIntegerOuZero(row.getCell(3).getStringCellValue()));
                    roubos.setAbril(converterParaIntegerOuZero(row.getCell(4).getStringCellValue()));
                    roubos.setMaio(converterParaIntegerOuZero(row.getCell(5).getStringCellValue()));
                    roubos.setJunho(converterParaIntegerOuZero(row.getCell(6).getStringCellValue()));
                    roubos.setJulho(converterParaIntegerOuZero(row.getCell(7).getStringCellValue()));
                    roubos.setAgosto(converterParaIntegerOuZero(row.getCell(8).getStringCellValue()));
                    roubos.setSetembro(converterParaIntegerOuZero(row.getCell(9).getStringCellValue()));
                    roubos.setOutubro(converterParaIntegerOuZero(row.getCell(10).getStringCellValue()));
                    roubos.setNovembro(converterParaIntegerOuZero(row.getCell(11).getStringCellValue()));
                    roubos.setDezembro(converterParaIntegerOuZero(row.getCell(12).getStringCellValue()));

                    roubos.setTotal(roubos.getJaneiro() + roubos.getFevereiro() + roubos.getMarco() +
                            roubos.getAbril() + roubos.getMaio() + roubos.getJunho() + roubos.getJulho() +
                            roubos.getAgosto() + roubos.getSetembro() + roubos.getOutubro() + roubos.getNovembro() +
                            roubos.getDezembro());

                    String sql = "INSERT INTO dados (dp, natureza, ano, janeiro, fevereiro, marco, abril, maio, junho, julho, agosto, setembro, outubro, novembro, dezembro, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    con.update(sql, roubos.getDp(), roubos.getNatureza(), roubos.getAno(), roubos.getJaneiro(),
                            roubos.getFevereiro(), roubos.getMarco(), roubos.getAbril(), roubos.getMaio(),
                            roubos.getJunho(), roubos.getJulho(), roubos.getAgosto(), roubos.getSetembro(),
                            roubos.getOutubro(), roubos.getNovembro(), roubos.getDezembro(), roubos.getTotal());

                    roubosExtraidos.add(roubos);
                    System.out.println(getDataHoraAtual() + " - Registro inserido");
                    qtdInseridos++;
                }
            }

            workbook.close();

            System.out.println(getDataHoraAtual() + " - Quantidade de registos inseridos: " + qtdInseridos);
            System.out.println("%s - Leitura do arquivo finalizada\n".formatted(getDataHoraAtual()));

            return roubosExtraidos;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String extrairDP(String nomeArquivo) {
        int inicio = nomeArquivo.indexOf("-") + 1;
        int fim = nomeArquivo.indexOf("_");

        return nomeArquivo.substring(inicio, fim).trim();
    }

    private Integer extrairAno(String nomeArquivo) {
        return Integer.parseInt(nomeArquivo.substring(nomeArquivo.indexOf("_") + 1, nomeArquivo.indexOf("_") + 5));
    }

    public static Integer converterParaIntegerOuZero(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
