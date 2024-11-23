package noticacoes.slack;

public class NotificacaoPolicia extends Slack {

    @Override
    public void enviarNotificacao(String mensagem) {
        String mensagemPolicial = "\uD83D\uDEA8 Notificação para a Polícia: " + mensagem;
        super.enviarNotificacao(mensagemPolicial);
    }
}