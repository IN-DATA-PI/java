import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;

public class LeitorExcel {

    // Formatter para formatar a data e hora no formato desejado
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    public List<Dados> extrairDados(String nomeArquivo, InputStream arquivo) {
        try {
            System.out.println("\nIniciando leitura do arquivo %s\n".formatted(nomeArquivo));

            // Criando um objeto Workbook a partir do arquivo recebido
            Workbook workbook;
            if (nomeArquivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(arquivo);
            } else {
                workbook = new HSSFWorkbook(arquivo);
            }
            // Acessando a primeira planilha
            Sheet sheet = workbook.getSheetAt(0);

            List<Dados> dadosExtraidos = new ArrayList<>();
            List<String> colunas = new ArrayList<>();

            InsignaConexao conexao = new InsignaConexao();
            JdbcTemplate con = conexao.getConexaoComBanco();

            // Iterando sobre as linhas da planilha
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    System.out.println("\nLendo cabeçalho");
                    for (int i = 0; i < 14; i++) {
                        String coluna = row.getCell(i).getStringCellValue();
                        System.out.println("Coluna " + i + ": " + coluna);
                        colunas.add(coluna);
                    }
                    System.out.println("--------------------");
                    continue;
                }

                String palavraProcurada = "Roubo";
                String valorCelula = row.getCell(0).getStringCellValue().toLowerCase();
                LocalDateTime dataHoraAtual = LocalDateTime.now();
                String dataHoraFormatada = dataHoraAtual.format(formatter);

                if (valorCelula.contains(palavraProcurada.toLowerCase())) {
                    // Construindo a SQL para inserir todas as colunas de uma vez
                    StringBuilder sql = new StringBuilder("INSERT INTO dados (");

                    // Adiciona os nomes das colunas ao comando SQL
                    for (int i = 0; i < colunas.size(); i++) {
                        sql.append(colunas.get(i));
                        if (i < colunas.size() - 1) {
                            sql.append(", ");
                        }
                    }
                    sql.append(") VALUES (");

                    // Adiciona os valores correspondentes das células ao comando SQL
                    for (int i = 0; i < colunas.size(); i++) {
                        if (i != 0) {
                            Double valorNum = converterParaDoubleOuZero(row.getCell(i).getStringCellValue());
                            sql.append(valorNum);
                        } else {
                            String valorStr = row.getCell(i).getStringCellValue();
                            sql.append("'").append(valorStr).append("'");
                        }

                        if (i < colunas.size() - 1) {
                            sql.append(", ");
                        }
                    }
                    sql.append(");");

                    // Executa o comando SQL
                    try {
                        con.execute(sql.toString());
                        System.out.println("[" + dataHoraFormatada + "] Registro inserido com sucesso: " + sql.toString());
                    } catch (Exception e) {
                        System.err.println("[" + dataHoraFormatada + "] Erro ao inserir registro: " + sql.toString());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("[" + dataHoraFormatada + "] Linha ignorada. Palavra procurada não encontrada: " + valorCelula);
                }
            }

            System.out.println(colunas);

            // Fechando o workbook após a leitura
            workbook.close();

            System.out.println("\nLeitura do arquivo finalizada\n");

            return dadosExtraidos;
        } catch (IOException e) {
            // Caso ocorra algum erro durante a leitura do arquivo uma exceção será lançada
            throw new RuntimeException(e);
        }
    }

    private LocalDate converterDate(Date data) {
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Double converterParaDoubleOuZero(String valor) {
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
