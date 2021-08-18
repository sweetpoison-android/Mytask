package com.wts.mytask.modelclass;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WebServiceInterface {

    String BASE_URL="http://api.cspsewa.in/api/";



    Retrofit retrofit= new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @FormUrlEncoded
    @POST("Login")
    Call<JsonObject> login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("deviceid") String deviceid );

    @FormUrlEncoded
    @POST("Forgotpassword")
    Call<JsonObject>  forgetpassword(
            @Field("username") String username,
            @Field("mobileno") String mobileno );

    @FormUrlEncoded
    @POST("Getbalance")
    Call<JsonObject> getBalance(
            @Field("userid") String userid);


    @POST("Getservice")
    Call<JsonObject> getService();

    @FormUrlEncoded
    @POST("GetOperators")
    Call<JsonObject> getOperators(
      @Field("serviceid") String serviceid);

   @FormUrlEncoded
    @POST("getusers")
    Call<JsonObject> getusers(@Field("userid") String userid);

   @FormUrlEncoded @POST("creditbalance")
    Call<JsonObject> creditbalance(
            @Field("userid") String userid,
            @Field("comment") String comment,
            @Field("amount") String amount,
            @Field("creditto") String creditto
   );

   @FormUrlEncoded
    @POST("debitbalance")
    Call<JsonObject> debitbalance(
           @Field("userid") String userid,
           @Field("comment") String comment,
           @Field("amount") String amount,
           @Field("creditto") String creditto
   );

    @FormUrlEncoded
    @POST("Creditreport")
    Call<JsonObject> Creditreport(
            @Field("userid") String userid,
            @Field("searchby") String searchBy,
            @Field("from") String fromDate,
            @Field("to") String toDate
    );

    @FormUrlEncoded
    @POST("Debitreport")
    Call<JsonObject> Debitreport(
            @Field("userid") String userid,
            @Field("searchby") String searchBy,
            @Field("from") String fromDate,
            @Field("to") String toDate
    );

    @FormUrlEncoded
    @POST("Getreport")
    Call<JsonObject> getReport(
            @Field("userid") String userid,
            @Field("searchby") String searchBy,
            @Field("from") String fromDate,
            @Field("to") String toDate
    );

    @FormUrlEncoded
    @POST("Getledger")
    Call<JsonObject> getLeger(
            @Field("userid") String userid,
            @Field("searchby") String searchBy,
            @Field("from") String fromDate,
            @Field("to") String toDate
    );

    @FormUrlEncoded
    @POST("ChangePassword")
    Call<JsonObject> changePassword(
            @Field("userid") String userId,
            @Field("password") String password,
            @Field("newpassword") String newPassword

    );

}
