package noticacoes.slack;

import java.io.IOException;

public class NotificacaoDelegado extends Slack {

    @Override
    protected String getWebhookUrl() {
        return "https://hooks.slack.com/services/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";
    }

    @Override
    public void enviarNotificacao(String mensagem) {
        String mensagemDelegado = "\uD83D\uDCCB Notificação para o Delegado: " + mensagem;
        super.enviarNotificacao(mensagemDelegado);
    }
}
