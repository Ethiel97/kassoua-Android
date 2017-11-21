package com.code.deventhusiast.alibaba.api;

import com.code.deventhusiast.alibaba.models.Category;
import com.code.deventhusiast.alibaba.models.Product;
import com.code.deventhusiast.alibaba.models.Result;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Ethiel on 19/10/2017.
 */

public interface APIService {

    //Register
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("register")
    Call<Result> createUser(
            @Field("country") String country,
            @Field("enterprise") String enterprise,
            @Field("lname") String lname,
            @Field("fname") String fname,
            @Field("email") String email,
            @Field("password") String password
    );


    //Login
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("login")
    Call<Result> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("logout")
    Call<Result> userLogout(
            @Field("api_token") String api_token
    );

    //Update user information
    @Headers("Accept: application/json")
    @Multipart
    @POST("users/{id}")
    Call<Result> updateUser(
            @Part("api_token") RequestBody api_token,
            @Path("id") int id,
            @Part MultipartBody.Part photo,
            @Part("country") RequestBody country,
            @Part("enterprise") RequestBody enterprise,
            @Part("gender") RequestBody gender,
            @Part("lname") RequestBody lname,
            @Part("fname") RequestBody fname,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("address") RequestBody address,
            @Part("job") RequestBody job,
            @Part("_method") RequestBody method
    );

    //Get user id
    @Headers("Accept: application/json")
    @GET("user/{id}")
    Call<Result> getUser(
            @Path("id") int id
    );

    //Products
    @Headers("Accept: application/json")
    @GET("products")
    Call<List<Product>> getProductsList();

    @Headers("Accept: application/json")
    @GET("products/users/{user_id}")
//    Call<List<Product>> getUserProducts(@Path("user_id") int id, @Header("Authorization") String authorization);
    Call<List<Product>> getUserProducts(@Path("user_id") int id);


    @Headers("Accept: application/json")
    @Multipart
    @POST("products")
    Call<Product> createProduct(
            @Part("api_token") RequestBody api_token,
            @Part MultipartBody.Part images,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("min_quantity") RequestBody min_quantity,
            @Part("category_id") RequestBody category_id
    );

    //Favorites
    @Headers("Accept: application/json")
    @GET("favorites")
    Call<List<Product>> getFavoritesList(@Header("Authorization") String authorization);

    @Headers("Accept: application/json")
    @GET("favorites/users/{user_id}")
    Call<List<Product>> getUsersFavoritesList(@Path("user_id") int id);

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("favorites")
    Call<String> loveProduct(
            @Field("api_token") String api_token,
            @Field("product_id") int product_id
    );

    @Headers("Accept: application/json")
    @DELETE("favorites/{id}")
    Call<String> unloveProduct(
            @Path("id") int id,
            @Header("Authorization") String authorization
    );

    //Categories

    @Headers("Accept: application/json")
    @GET("categories")
    Call<List<Category>> getCategoriesList();
}
