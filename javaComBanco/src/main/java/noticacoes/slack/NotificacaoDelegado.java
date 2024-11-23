package noticacoes.slack;

public class NotificacaoDelegado extends Slack {

    @Override
    public void enviarNotificacao(String mensagem) {
        String mensagemDelegado = "\uD83D\uDCCB Notificação para o noticacoes.slack.NotificacaoDelegado: " + mensagem;
        super.enviarNotificacao(mensagemDelegado);
    }
}
