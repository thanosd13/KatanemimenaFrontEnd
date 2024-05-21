package com.example.airbnb_app.requestClasses;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MyParcel implements Parcelable {

    private Message message;

    public MyParcel(Message message) {
        this.message = message;
    }

    protected MyParcel(Parcel in) {
    }

    public static final Creator<MyParcel> CREATOR = new Creator<MyParcel>() {
        @Override
        public MyParcel createFromParcel(Parcel in) {
            return new MyParcel(in);
        }

        @Override
        public MyParcel[] newArray(int size) {
            return new MyParcel[size];
        }
    };

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull android.os.Parcel dest, int flags) {
        dest.writeSerializable(message);
    }


}
