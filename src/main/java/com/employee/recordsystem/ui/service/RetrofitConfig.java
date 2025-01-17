package com.employee.recordsystem.ui.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {
    private static final String BASE_URL = "http://localhost:8080/api/";
    private static Retrofit retrofit = null;
    private static String authToken = null;

    public static void setAuthToken(String token) {
        authToken = token;
        retrofit = null; // Force recreation with new token
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            
            // Add auth token if available
            if (authToken != null) {
                httpClient.addInterceptor(chain -> {
                    okhttp3.Request original = chain.request();
                    okhttp3.Request request = original.newBuilder()
                        .header("Authorization", "Bearer " + authToken)
                        .method(original.method(), original.body())
                        .build();
                    return chain.proceed(request);
                });
            }

            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
        }
        return retrofit;
    }
}
