package com.example.smartpantrymanager.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**builds retrofit and shares it with every screen*/
/**retrofit is android library makes calling node.js api easier. instead of writing*/
public class ApiClient {

    //10.0.2.2-> how android emulator sees your PC
    //real phone, use PCs wifi ip
    //phone and PC on the same Wi-Fi
    //allow node.js through firewall
    public static final String BASE_URL = "http://10.0.2.2:3000/";

    private static ApiService service;

    private ApiClient() { }

    public static synchronized ApiService getService() {
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(ApiService.class);
        }
        return service;
    }
}