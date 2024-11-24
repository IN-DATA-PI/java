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
