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
    private List<Roubo> roubos;

    public LeitorExcel() {
        this.roubos = new ArrayList<>();
    }

    public List<Roubo> extrairDados(String nomeArquivo, InputStream arquivo, boolean anoFixo) {

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
                    Roubo roubo = new Roubo();
                    roubo.setDp(dp);
                    roubo.setAno(ano);
                    roubo.setNatureza(natureza);

                    roubo.setJaneiro(converterParaIntegerOuZero(row.getCell(1).getStringCellValue()));
                    roubo.setFevereiro(converterParaIntegerOuZero(row.getCell(2).getStringCellValue()));
                    roubo.setMarco(converterParaIntegerOuZero(row.getCell(3).getStringCellValue()));
                    roubo.setAbril(converterParaIntegerOuZero(row.getCell(4).getStringCellValue()));
                    roubo.setMaio(converterParaIntegerOuZero(row.getCell(5).getStringCellValue()));
                    roubo.setJunho(converterParaIntegerOuZero(row.getCell(6).getStringCellValue()));
                    roubo.setJulho(converterParaIntegerOuZero(row.getCell(7).getStringCellValue()));
                    roubo.setAgosto(converterParaIntegerOuZero(row.getCell(8).getStringCellValue()));
                    roubo.setSetembro(converterParaIntegerOuZero(row.getCell(9).getStringCellValue()));
                    roubo.setOutubro(converterParaIntegerOuZero(row.getCell(10).getStringCellValue()));
                    roubo.setNovembro(converterParaIntegerOuZero(row.getCell(11).getStringCellValue()));
                    roubo.setDezembro(converterParaIntegerOuZero(row.getCell(12).getStringCellValue()));

                    roubo.setTotal(roubo.getJaneiro() + roubo.getFevereiro() + roubo.getMarco() +
                            roubo.getAbril() + roubo.getMaio() + roubo.getJunho() + roubo.getJulho() +
                            roubo.getAgosto() + roubo.getSetembro() + roubo.getOutubro() + roubo.getNovembro() +
                            roubo.getDezembro());

                    String sql = "INSERT INTO roubo (dp, natureza, ano, janeiro, fevereiro, marco, abril, maio, junho, julho, agosto, setembro, outubro, novembro, dezembro, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    con.update(sql, roubo.getDp(), roubo.getNatureza(), roubo.getAno(), roubo.getJaneiro(),
                            roubo.getFevereiro(), roubo.getMarco(), roubo.getAbril(), roubo.getMaio(),
                            roubo.getJunho(), roubo.getJulho(), roubo.getAgosto(), roubo.getSetembro(),
                            roubo.getOutubro(), roubo.getNovembro(), roubo.getDezembro(), roubo.getTotal());

                    roubos.add(roubo);
                    System.out.println("Registro inserido: " + roubo);
                }
            }

            workbook.close();

            qtdBaixados++;
            System.out.println("\nLeitura do arquivo finalizada\n");

            return roubos;
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

    public List<Roubo> getRoubos() {
        return roubos;
    }

    public void setRoubos(List<Roubo> roubos) {
        this.roubos = roubos;
    }
}
