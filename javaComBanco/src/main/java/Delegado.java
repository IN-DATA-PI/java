public class Delegado extends Slack{

    @Override
    public void enviarNotificacao(String mensagem) {
        String mensagemDelegado = "\uD83D\uDCCB Notificação para o Delegado: " + mensagem;
        super.enviarNotificacao(mensagemDelegado);
    }
}
