package com.example.mytvs.retrofit;

import com.example.mytvs.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("reporting/vrm/api/test_new/int/gettabledata.php")
    Call<TableDataResponse> getData(@Body User user);

}
