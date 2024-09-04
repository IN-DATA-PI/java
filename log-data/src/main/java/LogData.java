import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class LogData {
    public static <list> void main(String[] args) throws InterruptedException{
        Scanner leia = new Scanner(System.in);
        Random random = new Random();

        DateTimeFormatter formatada = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime inicio = LocalDateTime.now();
        String dataInicio = inicio.format(formatada);

        List<String> sistema = new ArrayList<>(List.of("Inicializando o sitema " + dataInicio, "Verificando se arquivo é válido...", "Arquivo válido", "Iniciando inserção na base de dados...", "Dados inseridos com sucesso."));

        for (int i = 0; i < sistema.size(); i++) {
            int tempo = (random.nextInt(1,4) * 1000);
            System.out.println(sistema.get(i));
            Thread.sleep(tempo);
        }

        LocalDateTime encerramento = LocalDateTime.now();
        String dataEncerramento = encerramento.format(formatada);

        System.out.println("Desligamento do sistema " + dataEncerramento);
    }
}
