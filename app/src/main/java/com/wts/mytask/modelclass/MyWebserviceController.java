package com.wts.mytask.modelclass;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MyWebserviceController {

    public static final String baseurl = "http://api.cspsewa.in/api/";
    public static MyWebserviceController apicontroller;
    private retrofit2.Retrofit retrofit;

    MyWebserviceController()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(7000, TimeUnit.SECONDS)
                .readTimeout(7000, TimeUnit.SECONDS)
                .build();

        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(baseurl).client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    public static synchronized MyWebserviceController getInstance() {
        if (apicontroller == null) {
            apicontroller = new MyWebserviceController();
        }
        return apicontroller;
    }

    public WebServiceInterface getApi() {
        return retrofit.create(WebServiceInterface.class);
    }

}
