package ru.itsphere.subscription.common.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Create Retrofit object for server class
 */
public class RetrofitFactory {

    public static Retrofit getRetrofit(String serverUrl) {
        ExecutorService exec = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable);
            }
        });

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        return new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callbackExecutor(exec)
                .build();
    }
}
