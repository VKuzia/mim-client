package com.mimteam.mimclient.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
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

    public String get(String urlPrefix, Map<String, String> headersParams, Map<String, String> params) {
        return get(urlPrefix, headersParams, params, true);
    }

    public @Nullable String get(String urlPrefix, Map<String, String> headersParams, Map<String, String> params, boolean auth) {
        Request request = formGetRequest(urlPrefix, headersParams, params, auth);
        try {
            Response response = sendRequest(request);
            if (response != null) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String post(String urlPrefix, Map<String, String> headersParams, Map<String, String> params) {
        return post(urlPrefix, headersParams, params, true);
    }

    public @Nullable String post(String urlSuffix, Map<String, String> headersParams, Map<String, String> params, boolean auth) {
        Request request = formPostRequest(urlSuffix, headersParams, params, auth);
        try {
            Response response = sendRequest(request);
            if (response != null) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Request formGetRequest(String urlSuffix, Map<String, String> headersParams, @NotNull Map<String, String> params, boolean auth) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url + urlSuffix).newBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            urlBuilder.addQueryParameter(param.getKey(), param.getValue());
        }
        Headers headers = createHeaders(headersParams, auth);
        return new Request.Builder()
                .url(urlBuilder.build().url().toString())
                .headers(headers)
                .get()
                .build();
    }

    private Request formPostRequest(String urlSuffix, Map<String, String> headersParams, @NotNull Map<String, String> params, boolean auth) {
        FormEncodingBuilder formBody = new FormEncodingBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formBody.add(entry.getKey(), entry.getValue());
        }
        Headers headers = createHeaders(headersParams, auth);
        return new Request.Builder()
                .url(url + urlSuffix)
                .headers(headers)
                .post(formBody.build())
                .build();
    }

    private @NotNull Headers createHeaders(Map<String, String> headersParams, boolean auth) {
        if (auth) {
            if (userInfo.getToken() == null) {
                throw new NullTokenException();
            }
            headersParams.put("Authorization", "Bearer " + userInfo.getToken());
        }
        return Headers.of(headersParams);
    }

    private @Nullable Response sendRequest(Request request) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Response> callable = () -> {
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException(response + ": " + response.body().string());
            }
            return response;
        };
        Future<Response> future = executor.submit(callable);
        try {
            return future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class NullTokenException extends RuntimeException {
        public NullTokenException() {
            super("User token is null");
        }
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
