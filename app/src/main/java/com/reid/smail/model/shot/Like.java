package com.reid.smail.model.shot;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by reid on 2017/9/24.
 */

public class Like implements Parcelable{
    public long id;
    public String created_at;
    public Shot shot;

    protected Like(Parcel in) {
        id = in.readLong();
        created_at = in.readString();
        shot = in.readParcelable(Shot.class.getClassLoader());
    }

    public static final Creator<Like> CREATOR = new Creator<Like>() {
        @Override
        public Like createFromParcel(Parcel in) {
            return new Like(in);
        }

        @Override
        public Like[] newArray(int size) {
            return new Like[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(created_at);
        parcel.writeParcelable(shot, i);
    }
}
