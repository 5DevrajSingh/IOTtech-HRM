package com.androidproject.iottechhrm.retro;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("login")
    @FormUrlEncoded
    Call<String> loginApi(@Field("email")String email,@Field("password") String password);

    @POST("getEmployeeProfile")
    Call<String> getProfile(@Body RequestBody body);

    @Multipart
    @POST("updateEmployee")
    Call<String> updateProfile(@Part("id")String id,@Part("name") String name,@Part("email")String email,@Part("contact") String contact,@Part("p_address") String p_address,@Part("c_address") String c_address,@Part("gender") String gender,@Part("dob") String dob,@Part MultipartBody.Part multiOwnerImage);

    @POST("createEmployeeLeave")
    @FormUrlEncoded
    Call<String> createLeave(@Field("id")String id,@Field("category") String category,@Field("period") String period, @Field("startDate")String startDate,@Field("endDate") String endDate,@Field("lastDate") String lastDate,@Field("lastPeriod") String lastPeriod,@Field("lastCategory") String lastCategory,@Field("address") String address,@Field("reason") String reason,@Field("bottomSection") String bottomSection);

    @POST("employeeLeaveDetails")
    Call<String> leaveDetail(@Body RequestBody body);

    @POST("getEmployeeAttendance")
    Call<String> getEmployeeAttendance(@Body RequestBody body);

    @POST("overTime")
    Call<String> overTimeDetail(@Body RequestBody body);

    @POST("forgotPassword")
    @FormUrlEncoded
    Call<String> otpSend(@Field("email")String email);

    @POST("verifyOtp")
    @FormUrlEncoded
    Call<String> otpVerify(@Field("email")String email,@Field("otp") String code);

    @POST("changePassword")
    @FormUrlEncoded
    Call<String> changePassword(@Field("email")String email, @Field("password")String password, @Field("c_password")String c_password);

    @POST("employeeSalaryDetails")
    Call<String> salaryDetail(@Body RequestBody body);

    @POST("pfDetails")
    Call<String> pfDetail(@Body RequestBody body);

    @POST("employeeLoanDetails")
    Call<String> loanDetail(@Body RequestBody body);

    @POST("employeeInstalmentDetails")
    Call<String> loanInstalment(@Body RequestBody body);
}
