package ai.yantranet.smartagent.retrofithelper;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseImages {

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        retrofit = null;

        OkHttpClient client = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Keys.readFirstKey())
                .client(client)
                .build();
        return retrofit;
    }


}