package noticacoes.slack;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Slack {
    private static final HttpClient client = HttpClient.newHttpClient();

    protected String getWebhookUrl(){
        return "";
    }

    public void sendMessage(String content) throws IOException, InterruptedException {
        String jsonPayload = String.format("{\"text\": \"%s\"}", content); // Formata a mensagem como JSON

        HttpRequest request = HttpRequest.newBuilder(URI.create(getWebhookUrl()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(String.format("Status: %s", response.statusCode()));
        System.out.println(String.format("Response: %s", response.body()));
    }

    public void enviarNotificacao(String mensagem) {
        try {
            sendMessage(mensagem);
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao enviar mensagem para o noticacoes.slack.Slack: " + e.getMessage());
        }
    }
}
