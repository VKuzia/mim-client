package com.mimteam.mimclient.client;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HTTPClient {
    private final String url;
    private final UserInfo userInfo;
    private final OkHttpClient okHttpClient;

    public HTTPClient(UserInfo userInfo, String url) {
        this.userInfo = userInfo;
        this.url = url;
        this.okHttpClient = new OkHttpClient();
    }

    public Optional<String> get(String urlSuffix) {
        return get(urlSuffix, ImmutableMap.of());
    }

    public Optional<String> get(String urlSuffix, Map<String, String> params) {
        Request request = formGetRequest(urlSuffix, params);
        return sendRequest(request);
    }

    public Optional<String> post(String urlSuffix, Map<String, String> params) {
        Request request = formPostRequest(urlSuffix, params);
        return sendRequest(request);
    }

    private Request formGetRequest(String urlSuffix, @NotNull Map<String, String> params) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url + urlSuffix).newBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            urlBuilder.addQueryParameter(param.getKey(), param.getValue());
        }
        Headers headers = createHeaders();
        return new Request.Builder()
                .url(urlBuilder.build().url().toString())
                .headers(headers)
                .get()
                .build();
    }

    private Request formPostRequest(String urlSuffix, @NotNull Map<String, String> params) {
        FormEncodingBuilder formBody = new FormEncodingBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formBody.add(entry.getKey(), entry.getValue());
        }
        Headers headers = createHeaders();
        return new Request.Builder()
                .url(url + urlSuffix)
                .headers(headers)
                .post(formBody.build())
                .build();
    }

    private @NotNull Headers createHeaders() {
        return Headers.of("Authorization", "Bearer " + userInfo.getToken());
    }

    private @NotNull Optional<String> sendRequest(Request request) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> callable = () -> {
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                Log.d("HTTP CLIENT", response + ": " + response.body().string());
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            ResponseDTO responseDTO = mapper.readValue(response.body().string(), ResponseDTO.class);
            return responseDTO.getResponseMessage();
        };
        Future<String> future = executor.submit(callable);
        try {
            return Optional.fromNullable(future.get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return Optional.absent();
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
