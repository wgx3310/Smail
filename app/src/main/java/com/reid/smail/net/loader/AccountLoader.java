package com.reid.smail.net.loader;

import com.reid.smail.content.Constant;
import com.reid.smail.model.shot.Token;
import com.reid.smail.net.api.AccountApi;
import com.reid.smail.net.client.Loader;

import rx.Observable;

/**
 * Created by reid on 2017/10/17.
 */

public class AccountLoader extends Loader<AccountApi> {

    private AccountLoader(){

    }

    private static class Internal{
        private static final AccountLoader instance = new AccountLoader();
    }

    public static AccountLoader get(){
        return Internal.instance;
    }

    public Observable<Token> getToken(String code){
        return Impl().getToken(Constant.CLIENT_ID, Constant.CLIENT_SECRET, code)
                .compose(this.<Token>transformer());
    }

    @Override
    public String getBaseUrl() {
        return Constant.BASE_URL_WEBSITE;
    }
}
