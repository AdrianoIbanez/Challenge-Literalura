package br.alura.cruso.Challenge.Literalura.services.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {

    private final URI endereco = URI.create("https://gutendex.com/books/?search=");

    public String consumoAPI (String busca) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco + busca.replace(" ", "%20").toLowerCase()))
                .build();
        HttpResponse<String> response = null;

        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();
            return json;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
