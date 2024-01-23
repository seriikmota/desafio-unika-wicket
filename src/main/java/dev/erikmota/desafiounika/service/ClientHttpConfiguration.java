package dev.erikmota.desafiounika.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ClientHttpConfiguration {

    public HttpResponse<String> requestGet(String uri) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> requestPost(String uri, String json) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> requestDelete(String uri) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.noBody())
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> requestPut(String uri, String json) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> requestImportar(String uri, Path filePath) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "multipart/form-data; boundary=---011000010111000001101001")
                .method("POST", buildMultipartBody(filePath))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest.BodyPublisher buildMultipartBody(Path filePath) throws IOException {
        String boundary = "---011000010111000001101001";
        byte[] fileBytes = Files.readAllBytes(filePath);
        List<byte[]> multipartBytes = List.of(
                buildBytes("--%s\r\n", boundary),
                buildBytes("Content-Disposition: form-data; name=\"file\"; filename=\"%s\"\r\n", filePath.getFileName()),
                buildBytes("\r\n"),
                fileBytes,
                buildBytes("\r\n--%s--\r\n", boundary)
        );
        return HttpRequest.BodyPublishers.ofByteArrays(multipartBytes);
    }

    private byte[] buildBytes(String format, Object... args) {
        return String.format(format, args).getBytes(StandardCharsets.UTF_8);
    }
}
