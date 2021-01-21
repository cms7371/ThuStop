package com.thustop.thestop;


import com.thustop.thestop.model.Auth;
import com.thustop.thestop.model.FCMReg;
import com.thustop.thestop.model.PageResponse;
import com.thustop.thestop.model.Route;
import com.thustop.thestop.model.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApi {
    @POST("rest-auth/registration/")
    Call<Token> register(@Body Auth auth);
    @POST("rest-auth/login/")
    Call<Token> login(@Body Auth auth);
    @POST("api/devices/")
    Call<FCMReg> registerDevice(@Header("Authorization") String auth, @Body FCMReg reg);

    @GET("bus/api/routes/")
    Call<PageResponse<Route>> listRoutes(@Header("Authorization") String auth);
    @GET("bus/api/routes/{id}/")
    Call<Route> getRoute(@Header("Authorization") String auth, @Path("id") int id);

}

