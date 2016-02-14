package com.ckc.ersin.bebetrack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Mufasa on 2/15/2016.
 */
public interface BebetrackService {
    @POST("create")
    Call<AccountManager> create(@Body AccountManager phone);
}
