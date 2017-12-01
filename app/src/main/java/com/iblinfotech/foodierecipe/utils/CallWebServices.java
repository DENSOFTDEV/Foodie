package com.iblinfotech.foodierecipe.utils;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

public interface CallWebServices {

    // Register Activity
    @Multipart
    @POST("/Login/registerNewUser")
    void registerNewUser(@Part("username") String username,
                         @Part("email") String email,
                         @Part("gender") String gender,
                         @Part("device_id") String device_id,
                         @Part("device_type") String device_type,
                         @Part("userfile") TypedFile userfile,
                         @Part("password") String password,
                         @Part("fb_id") String fb_id, Callback<Response> response);

    //Forgot Password Activity
    @FormUrlEncoded
    @POST("/Comment/forgotPassword")
    void forgotPassword(@Field("email") String email, Callback<Response> response);

    //Login Activity
    @FormUrlEncoded
    @POST("/Login/checkLogin")
    void checkLogin(@Field("username") String username,
                    @Field("password") String password, Callback<Response> response);

    //Change Password Activity
    @FormUrlEncoded
    @POST("/Login/ChangePassword")
    void ChangePassword(@Field("user_id") String user_id,
                        @Field("old_password") String old_password,
                        @Field("new_password") String new_password, Callback<Response> response);

    @FormUrlEncoded
    @POST("/Login/registerNewUser")
    void checkFBregister(@Field("username") String username,
                         @Field("email") String email,
                         @Field("gender") String gender,
                         @Field("device_id") String device_id,
                         @Field("device_type") String device_type,
                         @Field("userfile") String userfile,
                         @Field("password") String password,
                         @Field("fb_id") String fb_id, Callback<Response> response);


    // MainActivity
    @POST("/Recipe/totalRecipeCounter")
    void gettotalRecipeCounter(Callback<Response> response);

    // CategoryItemListActivity
    @FormUrlEncoded
    @POST("/Recipe/getRecipeByCategory")
    void getRecipeByCategory(@Field("token") String token,
                             @Field("start") String start, Callback<Response> response);

    //MostCommented...
    @FormUrlEncoded
    @POST("/Recipe/getMostCommentedRecipe")
    void getMostCommentedRecipe(@Field("token") String token,
                                @Field("start") String start, Callback<Response> response);

    //MostPoppular...
    @FormUrlEncoded
    @POST("/Recipe/getMostPopularRecipe")
    void getMostPopularRecipe(@Field("token") String token,
                              @Field("start") String start, Callback<Response> response);

    // CategoryItemViewActivity
    @FormUrlEncoded
    @POST("/Recipe/getRecipeById")
    void getRecipeById(@Field("user_id") String user_id,
                       @Field("recipe_token") String recipe_token, Callback<Response> response);

    //DiscoverMode Activity
    @FormUrlEncoded
    @POST("/Recipe/getDiscoverMode")
    void getDiscoverMode(@Field("user_id") String user_id, Callback<Response> response);

    //Search Activity
    @FormUrlEncoded
    @POST("/Recipe/search")
    void getSearch(@Field("search") String search,
                   @Field("user_id") String user_id, Callback<Response> response);


    //Add to Favourite
    @FormUrlEncoded
    @POST("/Comment/FavouriteRecipe")
    void AddToFavouriteRecipe(@Field("recipe_token") String recipe_token,
                              @Field("user_id") String user_id, Callback<Response> response);

    //Favourite fragment
    @FormUrlEncoded
    @POST("/Recipe/getFavourite")
    void getFavourite(@Field("user_id") String user_id, Callback<Response> response);

    //Weekly Menu fragment
    @FormUrlEncoded
    @POST("/Comment/getWeeklyMenu")
    void getWeeklyMenu(@Field("user_token") String user_token,
                       @Field("user_id") String user_id, Callback<Response> response);

    //Weekly Menu fragment
    @FormUrlEncoded
    @POST("/Comment/AddReview")
    void AddReview(@Field("user_id") String user_id,
                   @Field("review_text") String review_text,
                   @Field("recipe_token") String recipe_token,
                   @Field("total_star") String total_star, Callback<Response> response);


    //    @Headers({"Content-Type: application/json"})
    @POST("/editprofile")
    void editUser(@Body HashMap<String, String> hashMap, Callback<Response> response);

    @POST("/fb_login")
    void fbLogin(@Body HashMap<String, String> hashMap, Callback<Response> response);

    @POST("/create_event")
    void createEvent(@Body HashMap<String, String> hashMap, Callback<Response> response);

    @GET("/logout/{token}")
    void logoutUser(@Path("token") String CustomerId, Callback<Response> response);

    @GET("/viewprofile/{token}")
    void viewUser(@Path("token") String CustomerId, Callback<Response> response);

    @POST("/getplaces")
    void getPlace(@Body HashMap<String, String> hashMap, Callback<Response> response);

    @POST("/userinterested")
    void addInterest(@Body HashMap<String, String> hashMap, Callback<Response> response);

    @POST("/geteventdetail")
    void getEventDetail(@Body HashMap<String, String> hashMap, Callback<Response> response);

    @GET("/getallevent")
    void getAllEvent(Callback<Response> response);

}