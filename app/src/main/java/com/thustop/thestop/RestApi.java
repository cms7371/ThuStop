package com.thustop.thestop;


import com.thustop.thestop.model.Auth;
import com.thustop.thestop.model.FCMReg;
import com.thustop.thestop.model.Login;
import com.thustop.thestop.model.PageResponse;
import com.thustop.thestop.model.Route;
import com.thustop.thestop.model.Ticket;
import com.thustop.thestop.model.Token;
import com.thustop.thestop.model.UserDetails;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApi {
    @POST("rest-auth/registration/")
    Call<Token> register(@Body Auth auth);
    @POST("rest-auth/login/")
    Call<Token> login(@Body Login login);
    @POST("api/devices/")
    Call<FCMReg> registerDevice(@Header("Authorization") String auth, @Body FCMReg reg);
    @GET("rest-auth/user/")
    Call<UserDetails> getUserDetails(@Header("Authorization") String auth);

    @GET("bus/api/routes/")
    Call<PageResponse<Route>> listRoutes(@Header("Authorization") String auth);
    @GET("bus/api/routes/{id}/")
    Call<Route> getRoute(@Header("Authorization") String auth, @Path("id") int id);

    @POST("bus/api/tickets/")
    Call<Ticket> postTicket(@Header("Authorization") String auth, @Body Ticket ticket);
    @PATCH("bus/api/tickets/{id}")
    Call<Ticket> updateTicket(@Header("Authorization") String auth, @Path("id") int id, @Body Ticket ticket);
    
}

