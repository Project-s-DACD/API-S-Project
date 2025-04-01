package org.example.apiServices;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public abstract class AviationProvider implements DataProvider {
    private static final OkHttpClient client = new OkHttpClient();

    protected String fetchData(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            } else {
                return null;
            }
        }
    }
}