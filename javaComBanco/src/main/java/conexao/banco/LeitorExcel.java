package conexao.banco;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LeitorExcel {
    private Integer qtdBaixados;

    public List<Dados> extrairDados(String nomeArquivo, InputStream arquivo, boolean anoFixo) {
        List<Dados> dadosExtraidos = new ArrayList<>();

        try {
            System.out.println("\nIniciando leitura do arquivo %s\n".formatted(nomeArquivo));

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

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                String palavraProcurada = "Roubo";
                String valorCelula = row.getCell(0).getStringCellValue().toLowerCase();
                String natureza = row.getCell(0).getStringCellValue();

                if (valorCelula.contains(palavraProcurada.toLowerCase())) {
                    Dados dados = new Dados();
                    dados.setDp(dp);
                    dados.setAno(ano);
                    dados.setNatureza(natureza);

                    dados.setJaneiro(converterParaIntegerOuZero(row.getCell(1).getStringCellValue()));
                    dados.setFevereiro(converterParaIntegerOuZero(row.getCell(2).getStringCellValue()));
                    dados.setMarco(converterParaIntegerOuZero(row.getCell(3).getStringCellValue()));
                    dados.setAbril(converterParaIntegerOuZero(row.getCell(4).getStringCellValue()));
                    dados.setMaio(converterParaIntegerOuZero(row.getCell(5).getStringCellValue()));
                    dados.setJunho(converterParaIntegerOuZero(row.getCell(6).getStringCellValue()));
                    dados.setJulho(converterParaIntegerOuZero(row.getCell(7).getStringCellValue()));
                    dados.setAgosto(converterParaIntegerOuZero(row.getCell(8).getStringCellValue()));
                    dados.setSetembro(converterParaIntegerOuZero(row.getCell(9).getStringCellValue()));
                    dados.setOutubro(converterParaIntegerOuZero(row.getCell(10).getStringCellValue()));
                    dados.setNovembro(converterParaIntegerOuZero(row.getCell(11).getStringCellValue()));
                    dados.setDezembro(converterParaIntegerOuZero(row.getCell(12).getStringCellValue()));

                    dados.setTotal(dados.getJaneiro() + dados.getFevereiro() + dados.getMarco() +
                            dados.getAbril() + dados.getMaio() + dados.getJunho() + dados.getJulho() +
                            dados.getAgosto() + dados.getSetembro() + dados.getOutubro() + dados.getNovembro() +
                            dados.getDezembro());

                    String sql = "INSERT INTO dados (dp, natureza, ano, janeiro, fevereiro, marco, abril, maio, junho, julho, agosto, setembro, outubro, novembro, dezembro, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    con.update(sql, dados.getDp(), dados.getNatureza(), dados.getAno(), dados.getJaneiro(),
                            dados.getFevereiro(), dados.getMarco(), dados.getAbril(), dados.getMaio(),
                            dados.getJunho(), dados.getJulho(), dados.getAgosto(), dados.getSetembro(),
                            dados.getOutubro(), dados.getNovembro(), dados.getDezembro(), dados.getTotal());

                    dadosExtraidos.add(dados);
                    System.out.println("Registro inserido: " + dados);
                }
            }

            workbook.close();

            System.out.println("\nLeitura do arquivo finalizada\n");

            return dadosExtraidos;
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

    public Integer getQtdBaixados() {
        return qtdBaixados;
    }

    public void setQtdBaixados(Integer qtdBaixados) {
        this.qtdBaixados = qtdBaixados;
    }
}
