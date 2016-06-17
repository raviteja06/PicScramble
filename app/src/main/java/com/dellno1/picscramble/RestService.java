package com.dellno1.picscramble;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RestService {
    public static Retrofit getRestService() {
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.connectTimeout(5000, TimeUnit.MILLISECONDS);
        b.readTimeout(5000, TimeUnit.MILLISECONDS);
        b.writeTimeout(5000, TimeUnit.MILLISECONDS);
        OkHttpClient okHttpClient = b.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl("https://api.flickr.com/services/feeds/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    public interface Service {
        @GET("photos_public.gne?format=json&nojsoncallback=?")
        Call<ResponseObject> getPictures();
    }
}
