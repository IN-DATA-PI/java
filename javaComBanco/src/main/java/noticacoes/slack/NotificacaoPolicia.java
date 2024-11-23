package noticacoes.slack;

public class NotificacaoPolicia extends Slack {

    @Override
    protected String getWebhookUrl() {
        return "https://hooks.slack.com/services/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";
    }

    @Override
    public void enviarNotificacao(String mensagem) {
        String mensagemPolicial = "\uD83D\uDEA8 Notificação para a Polícia: " + mensagem;
        super.enviarNotificacao(mensagemPolicial);
    }
}
