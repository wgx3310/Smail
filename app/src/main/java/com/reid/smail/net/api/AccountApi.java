package com.reid.smail.net.api;

import com.reid.smail.model.shot.Token;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by reid on 2017/9/2.
 */

public interface AccountApi {

    @FormUrlEncoded
    @POST("/oauth/token")
    Observable<Token> getToken(@Field("client_id") String clientId,
                               @Field("client_secret") String clientSecret,
                               @Field("code") String code);


}
