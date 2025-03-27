package org.example.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public abstract class FetchDataFromApi {
    private static final OkHttpClient client = new OkHttpClient();

    protected String fetchData() throws IOException {
        Request request = new Request.Builder()
                .url(urlLink())
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            } else {
                System.out.println("Respuesta vacía de la API.");
                return null;
            }
        }
    }

    protected abstract String urlLink();
}
