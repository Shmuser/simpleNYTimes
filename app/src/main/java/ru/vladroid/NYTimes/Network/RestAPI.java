package ru.vladroid.NYTimes.Network;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RestAPI {

    private static final String URL = "https://api.nytimes.com/svc/";

    private static RestAPI mRestAPI;

    private final NewsEndpoint newsEndpoint;

    public static synchronized RestAPI getInstance() {
        if (mRestAPI == null)
            mRestAPI = new RestAPI();
        return mRestAPI;
    }

    private RestAPI() {
        final OkHttpClient okHttpClient = buildClient();
        final Retrofit retrofit = buildRetrofit(okHttpClient);
        newsEndpoint = retrofit.create(NewsEndpoint.class);
    }

    private Retrofit buildRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private OkHttpClient buildClient() {

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(ApiKeyInterceptor.create())
                .build();
    }

    @NonNull
    public NewsEndpoint newsEndpoint() {
        return newsEndpoint;
    }
}
