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
