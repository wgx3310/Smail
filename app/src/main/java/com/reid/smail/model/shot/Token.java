package com.reid.smail.model.shot;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by reid on 2017/9/2.
 */

public class Token implements Parcelable{
//    val access_token: String, val token_type: String, val scope: String, val created_at: String
    public String access_token;
    public String token_type;
    public String scope;
    public String created_at;

    public Token(Parcel in) {
        access_token = in.readString();
        token_type = in.readString();
        scope = in.readString();
        created_at = in.readString();
    }

    public static final Creator<Token> CREATOR = new Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel in) {
            return new Token(in);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(access_token);
        parcel.writeString(token_type);
        parcel.writeString(scope);
        parcel.writeString(created_at);
    }
}
