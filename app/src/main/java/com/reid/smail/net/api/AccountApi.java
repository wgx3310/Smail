package com.reid.smail.net.api;

import com.reid.smail.model.Token;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by reid on 2017/9/2.
 */

public interface AccountApi {

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<Token> getToken(@Field("client_id") String clientId,
                         @Field("client_secret") String clientSecret,
                         @Field("code") String code);


}
