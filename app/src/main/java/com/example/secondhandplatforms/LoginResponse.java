package com.example.secondhandplatforms;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private UserData data;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public UserData getData() {
        return data;
    }
}
      class UserData {
    @SerializedName("id")
    private String id;

    @SerializedName("appKey")
    private String appKey;

    @SerializedName("username")
    private String username;

    @SerializedName("money")
    private int money;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("password")
    private String password;

    public String getId() {
        return id;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getUsername() {
        return username;
    }

    public int getMoney() {
        return money;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPassword() {
        return password;
    }
}
