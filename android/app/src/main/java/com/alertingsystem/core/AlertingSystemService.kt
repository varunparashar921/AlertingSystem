package com.alertingsystem.core

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AlertingSystemService {

    @FormUrlEncoded
    @POST("/alertingsystem/createUser")
    fun createUser(@Field("firstName") firstName: String,
                   @Field("lastName") lastName: String,
                   @Field("email") email: String,
                   @Field("password") password: String,
                   @Field("phNo") phNo: String,
                   @Field("deviceName") deviceName: String,
                   @Field("deviceOS") deviceOS: String,
                   @Field("deviceToken") token: String,
                   @Field("lat") lat: String,
                   @Field("lon") lon: String): Call<UserResponse>

    @FormUrlEncoded
    @POST("/alertingsystem/login")
    fun login(@Field("email") emailId: String,
              @Field("password") password: String): Call<LoginResponse>

    @GET("/alertingsystem/forgotPassword")
    fun forgotPassword(@Query("mail") emailId: String): Call<ApiResponse>

    @FormUrlEncoded
    @POST("/alertingsystem/changePassword")
    fun changePassword(@Field("userId") userId: String,
                       @Field("password") password: String): Call<ApiResponse>

    @FormUrlEncoded
    @POST("/alertingsystem/changeLocation")
    fun changeLocation(@Field("userId") userId: String,
                       @Field("lat") lat: String,
                       @Field("lon") lon: String): Call<ApiResponse>

    @FormUrlEncoded
    @POST("/alertingsystem/userSettings")
    fun setUserSettings(@Field("userId") userId: String,
                        @Field("notifyNotification") notifyNotification: Boolean): Call<UserResponse>

    @FormUrlEncoded
    @POST("/alertingsystem/registerToken")
    fun sendToken(@Field("userId") userId: String,
                  @Field("token") token: String): Call<ApiResponse>

    @FormUrlEncoded
    @POST("/alertingsystem/getTSunamiData")
    fun getTsunamiHistory(@Field("lat") lat: String,
                          @Field("lon") lon: String): Call<TSunamiResponse>

    @FormUrlEncoded
    @POST("/alertingsystem/getFireData")
    fun getFireHistory(@Field("lat") lat: String,
                       @Field("lon") lon: String): Call<FireResponse>

    @FormUrlEncoded
    @POST("/alertingsystem/getFloodData")
    fun getFloodHistory(@Field("lat") lat: String,
                        @Field("lon") lon: String): Call<FloodResponse>

    @FormUrlEncoded
    @POST("/alertingsystem/getEarthquakeData")
    fun getEarthQuakeHistory(@Field("lat") lat: String,
                             @Field("lon") lon: String): Call<EarthQuakeResponse>

}