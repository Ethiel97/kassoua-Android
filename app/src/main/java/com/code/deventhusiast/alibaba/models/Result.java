package com.code.deventhusiast.alibaba.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ethiel on 19/10/2017.
 */

public class Result {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private List<String> message;

    @SerializedName("user")
    private User user;

    public Result(Boolean error, List<String> message, User user) {
        this.error = error;
        this.message = message;
        this.user = user;
    }

    public Boolean getError() {
        return error;
    }

    public List<String> getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
