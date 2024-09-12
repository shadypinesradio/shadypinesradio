package com.shadypines.radio.api;

import com.shadypines.radio.model.BaseResponse;
import com.shadypines.radio.model.RecoModel;
import com.shadypines.radio.model.schecule.Schedule;
import com.shadypines.radio.view.activity.ads_response;
import com.shadypines.radio.view.authentication.changepassword.model.ChangePasswordResponse;
import com.shadypines.radio.view.authentication.forgot.model.ForgotResponse;
import com.shadypines.radio.view.authentication.signin.model.SignInResponse;
import com.shadypines.radio.view.authentication.signup.model.SignUpResponse;

import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by root on 12/2/18.
 */


public interface ApiInterface{

   /* @GET("status")
    Call<ResponseBody> getRadio();
*/
    @GET("first-data/")
    Single<ResponseBody> getRadio();


    @FormUrlEncoded
    @POST("Schedule/")
    Call<Schedule> getSchedule(@Field("email") String userId);


    @FormUrlEncoded
    @POST("Schedule/")
    Single<Schedule> getScheduleRx(@Field("email") String userId);



    @FormUrlEncoded
    @POST("create_user/")
    Call<BaseResponse> createUser(@Field("token") String token, @Field("userid") String userId);

    @FormUrlEncoded
    @POST("social_login/")
    Call<BaseResponse> socialSignInt(@Field("id") String id, @Field("email") String email, @Field(
            "name") String name, @Field("userid") String userId);


    @FormUrlEncoded
    @POST("listener_signup/")
    Call<BaseResponse> registerUser(@Field("fistname") String firstName,
                                    @Field("email") String email,
                                    @Field("password") String password,
                                    @Field("confirm_password") String confirmPassword,
                                    @Field("is_send_email") String isSendEmail,
                                    @Field("userid") String userId
                                     //@Field("username") String username
    );

    @FormUrlEncoded
    @POST("listeners_login/")
    Call<BaseResponse> loginUser(@Field("email") String email,
                                 @Field("password") String password
    );

    /**
     * socialGoogleSignIn
     */

    @FormUrlEncoded
    @POST("social_login/")
    Call<SignInResponse> getSocialGoogleSignIn(@Field("id") String id, @Field("email") String email, @Field(
            "name") String name, @Field("userid") String userId);

    /**
     * 1 : codding to uzzal..
     * for sign in...
     */
    @FormUrlEncoded
    @POST("listeners_login/")
    Call<SignInResponse> loginUserData(@Field("email") String email,
                                       @Field("password") String password,
                                       @Field("userid") String userid
    );

    /**
     * 2 : codding to uzzal.
     * for sign up
     */
    @FormUrlEncoded
    @POST("listener_signup/")
    Call<SignUpResponse> signUpUserData(@Field("fistname") String firstName,
                                        @Field("lastname") String lastName,
                                        @Field("email") String email,
                                        @Field("password") String password,
                                        @Field("confirm_password") String confirmPassword,
                                        @Field("is_send_email") String isSendEmail,
                                        @Field("userid") String userid,
                                         @Field("username") String username

    );

    /**
     * 3 : codding to uzzal.
     * for reset password.
     */
    @FormUrlEncoded
    //@POST("reset-link/")
    @POST
    Call<ForgotResponse> resetPasswordData(@Url String url,
                                           @Field("email") String email);


    @GET
    Call<ads_response> adsData(@Url String url);

    /**
     * 4 :
     * uzzal for codding change password.
     */
    @FormUrlEncoded
    @POST("change-password/")
    Call<ChangePasswordResponse> changePasswordData(
            @Field("email") String email,
            @Field("current_password") String currentPassword,
            @Field("new_password") String newPassword);


    @FormUrlEncoded
    @POST("is_subscribe/")
    Call<BaseResponse> subscribeToShow(@Field("schudleId") String showId,
                                       @Field("email") String email);


    @FormUrlEncoded
    @POST
    Call<BaseResponse> resetPassword(@Url String url, @Field("email") String email);

 @FormUrlEncoded
 @POST("toggle-show/") // Replace with your actual API endpoint
 Call<RecoModel> updateToggleState(
         @Field("show_id") String showId,
         @Field("is_active") String isActive
 );
}

