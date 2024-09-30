import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class TesteDados {
    public static void main(String[] args) {
        InsignaConexao conexao = new InsignaConexao();
        JdbcTemplate con = conexao.getConexaoComBanco();

//        con.execute("""
//                create table dados (
//                id int primary key auto_increment,
//                dp varchar(100),  // Adicionando o campo dp
//                natureza varchar(50),
//                ano int,
//                janeiro int,
//                fevereiro int,
//                marco int,
//                abril int,
//                maio int,
//                junho int,
//                julho int,
//                agosto int,
//                setembro int,
//                outubro int,
//                novembro int,
//                dezembro int,
//                total int
//                );""");

//        con.execute("""
//                Drop table dados""");

        con.update("""
                INSERT INTO dados (dp, natureza, ano, janeiro, fevereiro, marco, abril, maio, junho, julho, agosto, setembro, outubro, novembro, dezembro, total)
                VALUES ('Delegacia Central', 'Roubo', 2023, 50, 60, 55, 45, 70, 65, 80, 75, 90, 85, 60, 55, 790);
                """);

        List<Dados> dadosDoBanco = con.query("SELECT * FROM dados", new BeanPropertyRowMapper<>(Dados.class));
        System.out.println(dadosDoBanco);

        Dados dadoNovo = new Dados();
        dadoNovo.setDp("Delegacia Leste");  // Atribuindo valor ao campo dp
        dadoNovo.setNatureza("Roubo");
        dadoNovo.setAno(2023);
        dadoNovo.setJaneiro(50);
        dadoNovo.setFevereiro(60);
        dadoNovo.setMarco(55);
        dadoNovo.setAbril(45);
        dadoNovo.setMaio(70);
        dadoNovo.setJunho(65);
        dadoNovo.setJulho(80);
        dadoNovo.setAgosto(75);
        dadoNovo.setSetembro(90);
        dadoNovo.setOutubro(85);
        dadoNovo.setNovembro(60);
        dadoNovo.setDezembro(55);
        dadoNovo.setTotal(1000);

        String sql = "INSERT INTO dados (dp, natureza, ano, janeiro, fevereiro, marco, abril, maio, junho, julho, agosto, setembro, outubro, novembro, dezembro, total) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        con.update(sql,
                dadoNovo.getDp(),
                dadoNovo.getNatureza(),
                dadoNovo.getAno(),
                dadoNovo.getJaneiro(),
                dadoNovo.getFevereiro(),
                dadoNovo.getMarco(),
                dadoNovo.getAbril(),
                dadoNovo.getMaio(),
                dadoNovo.getJunho(),
                dadoNovo.getJulho(),
                dadoNovo.getAgosto(),
                dadoNovo.getSetembro(),
                dadoNovo.getOutubro(),
                dadoNovo.getNovembro(),
                dadoNovo.getDezembro(),
                dadoNovo.getTotal());
    }
}
