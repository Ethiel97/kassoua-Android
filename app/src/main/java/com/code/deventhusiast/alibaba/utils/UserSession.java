package com.code.deventhusiast.alibaba.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.code.deventhusiast.alibaba.models.User;

import java.util.HashMap;

/**
 * Created by Ethiel on 19/10/2017.
 */

public class UserSession {

    private static final String SHARED_PREF_NAME = "usersession";
    private static final String USER_ID = "0";
    private static final String USER_COUNTRY = "user_country";
    private static final String USER_ENTERPRISE = "user_enterprise";
    private static final String USER_FNAME = "user_fname";
    private static final String USER_LNAME = "user_lname";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_GENDER = "user_gender";
    private static final String USER_JOB = "user_job";
    private static final String USER_ADDRESS = "user_address";
    private static final String USER_PHONE = "user_phone";
    private static final String USER_PHOTO = "user_photo";
    private static final String USER_FAVS = "user_favs";
    private static final String USER_API_TOKEN = "user_apiToken";
    private static UserSession session;
    private static Context ctx;
    public static HashMap<String, Integer> favs = new HashMap<>();

    UserSession(Context context) {
        ctx = context;
    }

    public static synchronized UserSession Instance(Context context) {
        if (session == null) {
            session = new UserSession(context);
        }
        return session;
    }

    public boolean UserLogin(User user) {
        SharedPreferences preferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(USER_ID, user.getId());
        editor.putString(USER_FNAME, user.getFname());
        editor.putString(USER_LNAME, user.getLname());
        editor.putString(USER_EMAIL, user.getEmail());
        editor.putString(USER_ENTERPRISE, user.getEnterprise());
        editor.putString(USER_COUNTRY, user.getCountry());
        editor.putString(USER_GENDER, user.getGender());
        editor.putString(USER_JOB, user.getJob());
        editor.putString(USER_ADDRESS, user.getAddress());
        editor.putString(USER_PHONE, user.getPhone());
        editor.putString(USER_PHOTO, user.getPhoto());
        editor.putString(USER_API_TOKEN, user.getApiToken());
        editor.apply();
        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences preferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(USER_EMAIL, null) != null;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(USER_ID, 0),
                sharedPreferences.getString(USER_COUNTRY, null),
                sharedPreferences.getString(USER_ENTERPRISE, null),
                sharedPreferences.getString(USER_LNAME, null),
                sharedPreferences.getString(USER_FNAME, null),
                sharedPreferences.getString(USER_EMAIL, null),
                sharedPreferences.getString(USER_GENDER, null),
                sharedPreferences.getString(USER_JOB, null),
                sharedPreferences.getString(USER_ADDRESS, null),
                sharedPreferences.getString(USER_PHONE, null),
                sharedPreferences.getString(USER_PHOTO, null),
                sharedPreferences.getString(USER_API_TOKEN, null)
        );
    }

    public void updateUser(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt(USER_ID, user.getId());
        editor.putString(USER_FNAME, user.getFname());
        editor.putString(USER_LNAME, user.getLname());
        editor.putString(USER_EMAIL, user.getEmail());
        editor.putString(USER_ENTERPRISE, user.getEnterprise());
        editor.putString(USER_COUNTRY, user.getCountry());
        editor.putString(USER_GENDER, user.getGender());
        editor.putString(USER_JOB, user.getJob());
        editor.putString(USER_ADDRESS, user.getAddress());
        editor.putString(USER_PHONE, user.getPhone());
        editor.putString(USER_PHOTO, user.getPhoto());
//        editor.putString(USER_API_TOKEN, user.getApiToken());
        editor.apply();

    }

    public boolean logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

}
