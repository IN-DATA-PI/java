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

            InsignaConexao conexao = new InsignaConexao();
            JdbcTemplate con = conexao.getConexaoComBanco();

            // Iterando sobre as linhas da planilha
            for (Row row : sheet) {
                Dados dados = new Dados();

                if (row.getRowNum() == 0) {
                    System.out.println("\nLendo cabeçalho");
                    String coluna = row.getCell(1).getStringCellValue();
                    for (int i = 0; i < 14; i++) {
                        //String coluna = row.getCell(i).getStringCellValue();
                        System.out.println("Coluna " + i + ": " + coluna);
                    }
                    System.out.println("--------------------");
                    continue;
                }

                Cell cell = row.getCell(1);
                if (cell != null && cell.getCellType() == CellType.STRING){
                    String cellValue = cell.getStringCellValue();

                    // Verifica se a célula contém apenas números
                    try {
                        int intValue = Integer.parseInt(cellValue);
                        System.out.println("Valor inteiro convertido: " + intValue);
                    } catch (NumberFormatException e) {
                        System.out.println("Erro: o valor '" + cellValue + "' não é um número inteiro válido.");
                    }
                }else if (cell != null && cell.getCellType() == CellType.NUMERIC){
                    // Caso a célula seja numérica, podemos pegar diretamente o valor como int
                    int intValue = (int) cell.getNumericCellValue();
                    System.out.println("Valor numérico diretamente: " + intValue);
                }else {
                    System.out.println("A célula está vazia ou contém um tipo inválido.");
                }

                if (row.getRowNum() > 17){
//                    con.execute("""
//                    INSERT INTO dados (natureza)
//                    VALUES ('%s');
//                    """.formatted(row.getCell(0).getStringCellValue()));
                    con.execute("""
                    INSERT INTO dados (janeiro)
                    VALUES ('%d');
                    """.formatted((Integer.parseInt(row.getCell(1).getStringCellValue()))));
//                    con.execute("""
//                    INSERT INTO dados (fevereiro)
//                    VALUES ('%s');
//                    """.formatted(row.getCell(2).getStringCellValue()));

                }

                // Extraindo valor das células e criando objeto Livro
                System.out.println("Lendo linha " + row.getRowNum());

//                dados.setIdDados((int) row.getCell(0).getNumericCellValue());
//                dados.setDp(row.getCell(1).getStringCellValue());

               // dados.setNatureza(row.getCell(0).getStringCellValue());
//                dados.setAno((int)row.getCell(3).getNumericCellValue());

 //               String stringValue = row.getCell(1).getStringCellValue();

                // Remove tudo o que não for número
//                String somenteNumeros = stringValue.replaceAll("[^0-9]", "");

                // Define o valor de Janeiro com o número
//                dados.setJaneiro(somenteNumeros);
//                con.execute("""
//                INSERT INTO dados (janeiro)
//                VALUES ('%s');
//                """.formatted(row.getCell(1).getStringCellValue()));

//                dados.setFevereiro((int)row.getCell(2).getNumericCellValue());
//                dados.setMarco((int)row.getCell(3).getNumericCellValue());
//                dados.setAbril((int)row.getCell(4).getNumericCellValue());
//                dados.setMaio((int)row.getCell(5).getNumericCellValue());
//                dados.setJunho((int)row.getCell(6).getNumericCellValue());
//                dados.setJulho((int)row.getCell(7).getNumericCellValue());
//                dados.setAgosto((int)row.getCell(8).getNumericCellValue());
//                dados.setSetembro((int)row.getCell(9).getNumericCellValue());
//                dados.setOutubro((int)row.getCell(10).getNumericCellValue());
//                dados.setNovembro((int)row.getCell(11).getNumericCellValue());
//                dados.setDezembro((int)row.getCell(12).getNumericCellValue());
//                dados.setTotal((int)row.getCell(13).getNumericCellValue());

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
}