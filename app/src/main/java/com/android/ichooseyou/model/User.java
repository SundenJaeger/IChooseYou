package com.android.ichooseyou.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.net.URL;

public class User implements Parcelable {
    private String name, password, email;

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    protected User(Parcel in) {
        name = in.readString();
        password = in.readString();
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(email);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
