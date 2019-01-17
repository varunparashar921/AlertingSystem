package com.alertingsystem.core;

import com.google.gson.annotations.SerializedName;

public class LoginResponse extends ApiResponse {

    @SerializedName("data")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
