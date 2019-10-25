package ru.vladroid.NYTimes.Network;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class ApiKeyInterceptor implements Interceptor {

    private static final String TOP_STORIES_API_KEY = "blablabla";
    private static final String API_KEY_HEADER_NAME = "api-key";

    public static ApiKeyInterceptor create() {
        return new ApiKeyInterceptor();
    }

    private ApiKeyInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter(API_KEY_HEADER_NAME, TOP_STORIES_API_KEY)
                .build();

        Request.Builder requestBuilder = original.newBuilder()
                .url(url);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}