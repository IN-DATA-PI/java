import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class InsignaConexao {

    private JdbcTemplate conexaoComBanco;

    public InsignaConexao(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/Insigna?useSSL=false&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("102030");

        conexaoComBanco = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getConexaoComBanco() {
        return conexaoComBanco;
    }

    public void setConexaoComBanco(JdbcTemplate conexaoComBanco) {
        this.conexaoComBanco = conexaoComBanco;
    }
}
