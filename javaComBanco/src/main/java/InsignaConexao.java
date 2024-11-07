import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class InsignaConexao {

    private JdbcTemplate conexaoComBanco;
//
    public InsignaConexao(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost" +
                ":3306/Insigna?useSSL=false&serverTimezone=UTC");
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
