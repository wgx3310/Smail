package com.reid.smail.model.shot;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by reid on 2017/9/23.
 */

public class Bucket implements Parcelable {

//    {
//        "id" : 2754,
//            "name" : "Great Marks",
//            "description" : "Collecting superb brand marks from the <a href=\"https://dribbble.com\">Dribbbleverse</a>.",
//            "shots_count" : 251,
//            "created_at" : "2011-05-20T21:05:55Z",
//            "updated_at" : "2014-02-21T16:37:12Z"
//    }

    public long id;
    public String name;
    public String description;
    public int shots_count;
    public String created_at;
    public String updated_at;

    protected Bucket(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        shots_count = in.readInt();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static final Creator<Bucket> CREATOR = new Creator<Bucket>() {
        @Override
        public Bucket createFromParcel(Parcel in) {
            return new Bucket(in);
        }

        @Override
        public Bucket[] newArray(int size) {
            return new Bucket[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeInt(shots_count);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
    }

    @Override
    public String toString() {
        return "Bucket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", shots_count=" + shots_count +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
