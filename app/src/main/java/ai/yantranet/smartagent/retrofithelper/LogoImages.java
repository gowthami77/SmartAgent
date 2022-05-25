package ai.yantranet.smartagent.retrofithelper;

import ai.yantranet.smartagent.model.Data;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface LogoImages {


    @GET("/{second}")
    Call<Data> fetch(@Path("second") String second);

}


