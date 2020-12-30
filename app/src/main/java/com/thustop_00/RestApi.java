package com.thustop_00;


import com.thustop_00.model.Auth;
import com.thustop_00.model.FCMReg;
import com.thustop_00.model.PageResponse;
import com.thustop_00.model.Route;
import com.thustop_00.model.Token;
import com.thustop_00.model.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApi {
    @POST("rest-auth/registration/")
    Call<Token> register(@Body UserData userData);
    @POST("rest-auth/login/")
    Call<Token> login(@Body Auth auth);
    @POST("api/devices/")
    Call<FCMReg> registerDevice(@Header("Authorization") String auth, @Body FCMReg reg);

    @GET("bus/api/routes/")
    Call<PageResponse<Route>> listRoutes(@Header("Authorization") String auth);
    @GET("bus/api/routes/{id}/")
    Call<Route> getRoute(@Header("Authorization") String auth, @Path("id") int id);

}

