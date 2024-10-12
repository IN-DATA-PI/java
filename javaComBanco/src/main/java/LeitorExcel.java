import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

public class LeitorExcel {

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
                Dados dados = new Dados();

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

                Integer aux = 1;
                String auxCol = colunas.get(1);
                if (row.getRowNum() > 17){
//                    con.execute("""
//                    INSERT INTO dados (natureza)
//                    VALUES ('%s');
//                    """.formatted(row.getCell(0).getStringCellValue()));
                    for (int i = 0; i <= 12; i++) {
                        con.execute("""
                        INSERT INTO dados (%s)
                        VALUES (%d);
                        """.formatted(colunas.get(i + 1),converterParaIntOuZero(row.getCell(aux).getStringCellValue())));
                        aux++;
                    }
                }

                System.out.println(colunas);
                System.out.println("Lendo linha " + row.getRowNum());


                dadosExtraidos.add(dados);
            }


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

    public static Integer converterParaIntOuZero(String valor) {
        try {
            // Tenta converter a String para int
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            // Se falhar, retorna 0
            return 0;
        }
    }
}