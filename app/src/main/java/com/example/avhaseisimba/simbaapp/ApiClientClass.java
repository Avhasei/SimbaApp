package com.example.avhaseisimba.simbaapp;


/* *
 * Class is used to create API calls to the services
 * Created by Avhasei Simba
 * */

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiClientClass {

    public static String ApiUrl = "http://jsonplaceholder.typicode.com/users";

    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
