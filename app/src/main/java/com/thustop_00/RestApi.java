package com.thustop_00;


import com.thustop_00.model.FCMReg;
import com.thustop_00.model.Token;
import com.thustop_00.model.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RestApi {
    @POST("rest-auth/registration/")
    Call<Token> register(@Body UserData userData);
    @POST("rest-auth/login/")
    Call<Token> login(@Body UserData userData);
    @POST("api/devices/")
    Call<FCMReg> registerDevice(@Header("Authorization") String auth, @Body FCMReg reg);
}

