package com.tranxitpro.partner.data.network;

import com.tranxitpro.partner.data.network.model.CancelResponse;
import com.tranxitpro.partner.data.network.model.Card;
import com.tranxitpro.partner.data.network.model.CheckVersion;
import com.tranxitpro.partner.data.network.model.DisputeResponse;
import com.tranxitpro.partner.data.network.model.DriverDocumentResponse;
import com.tranxitpro.partner.data.network.model.EarningsList;
import com.tranxitpro.partner.data.network.model.EstimateFare;
import com.tranxitpro.partner.data.network.model.ForgotResponse;
import com.tranxitpro.partner.data.network.model.Help;
import com.tranxitpro.partner.data.network.model.HistoryDetail;
import com.tranxitpro.partner.data.network.model.HistoryList;
import com.tranxitpro.partner.data.network.model.NotificationManager;
import com.tranxitpro.partner.data.network.model.Rating;
import com.tranxitpro.partner.data.network.model.RequestDataResponse;
import com.tranxitpro.partner.data.network.model.SettingsResponse;
import com.tranxitpro.partner.data.network.model.Summary;
import com.tranxitpro.partner.data.network.model.TimerResponse;
import com.tranxitpro.partner.data.network.model.Token;
import com.tranxitpro.partner.data.network.model.TripResponse;
import com.tranxitpro.partner.data.network.model.User;
import com.tranxitpro.partner.data.network.model.UserResponse;
import com.tranxitpro.partner.data.network.model.WalletMoneyAddedResponse;
import com.tranxitpro.partner.data.network.model.WalletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("api/user/checkversion")
    Observable<CheckVersion> checkversion(@FieldMap HashMap<String, Object> params);

    @FormUrlEncoded
    @POST("api/provider/oauth/token")
    Observable<User> login(@FieldMap HashMap<String, Object> params);

    @FormUrlEncoded
    @POST("api/provider/oauth/token")
    Observable<User> refreshToken();

    @FormUrlEncoded
    @POST("api/provider/auth/google")
    Observable<Token> loginGoogle(@FieldMap HashMap<String, Object> params);

    @FormUrlEncoded
    @POST("api/provider/auth/facebook")
    Observable<Token> loginFacebook(@FieldMap HashMap<String, Object> params);

    @GET("https://maps.googleapis.com/maps/api/geocode/json?latlng=8.403521,124.590141&sensor=true&key=AIzaSyCg1Hwub1dxL5-Nh7roJ-sMncjNT-LqC2o")
    Observable<Object> getPlaces();

    @Multipart
    @POST("api/provider/register")
    Observable<User> register(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> file);

    @FormUrlEncoded
    @POST("api/provider/verify")
    Observable<Object> verifyEmail(@Field("email") String email);

    @GET("api/provider/profile")
    Observable<UserResponse> getProfile();

    @Multipart
    @POST("api/provider/profile")
    Observable<UserResponse> profileUpdate(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/provider/logout")
    Observable<Object> logout(@FieldMap HashMap<String, Object> params);

    @GET("api/provider/trip?")
    Observable<TripResponse> getTrip(@QueryMap HashMap<String, Object> params);

    @FormUrlEncoded
    @POST("api/provider/profile/available")
    Observable<Object> providerAvailable(@FieldMap HashMap<String, Object> params);

    @FormUrlEncoded
    @POST("api/provider/forgot/password")
    Observable<ForgotResponse> forgotPassword(@FieldMap HashMap<String, Object> params);

    @FormUrlEncoded
    @POST("api/provider/reset/password")
    Observable<Object> resetPassword(@FieldMap HashMap<String, Object> params);

    @FormUrlEncoded
    @POST("api/provider/profile/password")
    Observable<Object> changePassword(@FieldMap HashMap<String, Object> params);

    @FormUrlEncoded
    @POST("api/provider/trip/{request_id}")
    Observable<Object> acceptRequest(@Field("dummy") String dummy, @Path("request_id") Integer request_id);

    @DELETE("api/provider/trip/{request_id}")
    Observable<Object> rejectRequest(@Path("request_id") Integer request_id);

    @FormUrlEncoded
    @POST("api/provider/cancel")
    Observable<Object> cancelRequest(@FieldMap HashMap<String, Object> params);

    @FormUrlEncoded
    @POST("api/provider/trip/{request_id}")
    Observable<Object> updateRequest(@FieldMap HashMap<String, Object> params,
                                     @Path("request_id") Integer request_id);

    @FormUrlEncoded
    @POST("api/provider/trip/{request_id}/rate")
    Observable<Rating> ratingRequest(@FieldMap HashMap<String, Object> params, @Path("request_id") Integer request_id);

    @GET("api/provider/requests/history")
    Observable<List<HistoryList>> getHistory();

    @GET("api/provider/requests/history/details?")
    Observable<HistoryDetail> getHistoryDetail(@Query("request_id") String request_id);

    @GET("api/provider/requests/upcoming")
    Observable<List<HistoryList>> getUpcoming();

    @GET("api/provider/requests/upcoming/details?")
    Observable<HistoryDetail> getUpcomingDetail(@Query("request_id") String request_id);

    @DELETE("api/provider/logout/{user_id}")
    Observable<Object> logout(@Path("user_id") Integer user_id);

    @GET("api/provider/target")
    Observable<EarningsList> getEarnings();

    @FormUrlEncoded
    @POST("api/provider/summary")
    Observable<Summary> getSummary(@Field("data") String data);

    @GET("api/provider/help")
    Observable<Help> getHelp();

    @GET("/api/provider/wallettransaction")
    Observable<WalletResponse> getWalletTransactions();

    @FormUrlEncoded
    @POST("/api/provider/add/money")
    Observable<WalletMoneyAddedResponse> addMoney(@FieldMap HashMap<String, Object> obj);

    @GET("api/provider/transferlist")
    Observable<RequestDataResponse> getRequestAmtData();

    @FormUrlEncoded
    @POST("/api/provider/requestamount")
    Observable<Object> postRequestAmt(@Field("amount") double amount, @Field("type") String type);

    @GET("/api/provider/requestcancel?")
    Observable<Object> getRemoveRequestAmt(@Query("id") int id);

//    @Headers({"Content-Type: application/json", "Authorization: key=" + BuildConfig.FCM_SERRVER_KEY})
//    @POST("fcm/send")
//    Observable<Object> sendFcm(@Body JsonObject jsonObject);

    @FormUrlEncoded
    @POST("/api/provider/chat")
    Observable<Object> postChatItem(
            @Field("sender") String sender,
            @Field("user_id") String user_id,
            @Field("message") String message);

    @FormUrlEncoded
    @POST("/api/provider/profile/language")
    Observable<Object> postChangeLanguage(@Field("language") String language);

    @GET("/api/provider/profile/documents")
    Observable<DriverDocumentResponse> getDriverDocuments();

    @Multipart
    @POST("api/provider/profile/documents/store")
    Observable<DriverDocumentResponse> postUploadDocuments(@PartMap Map<String, RequestBody> params,
                                                           @Part List<MultipartBody.Part> file);

    @FormUrlEncoded
    @POST("api/provider/providercard/destroy")
    Observable<Object> deleteCard(@Field("card_id") String cardId,
                                  @Field("_method") String method);

    @GET("api/provider/providercard")
    Observable<List<Card>> card();

    @FormUrlEncoded
    @POST("api/provider/providercard")
    Observable<Object> addcard(@Field("stripe_token") String stripeToken);

    @FormUrlEncoded
    @POST("api/provider/providercard/update")
    Observable<Object> changeCard(@Field("card_id") String cardId,
                                  @Field("_method") String method);

    @GET("/api/provider/settings")
    Observable<SettingsResponse> getSettings();

    @FormUrlEncoded
    @POST("api/provider/verify-credentials")
    Observable<Object> verifyCredentials(@Field("country_code") String countryCode, @Field("mobile") String mobile);

    @GET("api/provider/reasons")
    Observable<List<CancelResponse>> getCancelReasons();

    @FormUrlEncoded
    @POST("api/provider/dispute")
    Observable<Object> dispute(@FieldMap HashMap<String, Object> params);

    //    1 for start and 0 or end
    @FormUrlEncoded
    @POST("api/provider/waiting")
    Observable<TimerResponse> waitingTime(@Field("status") String status,
                                          @Field("id") String requestId);

    //    1 for start and 0 or end
    @FormUrlEncoded
    @POST("api/provider/waiting")
    Observable<TimerResponse> CheckWaitingTime(@Field("id") String requestId);

    @GET("/api/provider/notifications/provider")
    Observable<List<NotificationManager>> getNotificationManager();

    @POST("api/provider/requests/instant/ride")
    Observable<TripResponse> requestInstantRide(@QueryMap Map<String, Object> params);

    @GET("api/user/estimated/fare_without_auth")
    Observable<EstimateFare> estimateFare(@QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("api/provider/dispute-list")
    Observable<List<DisputeResponse>> getDispute(@Field("dispute_type")String dispute_type);

    @FormUrlEncoded
    @POST("api/provider/drop-item")
    Observable<Object> dropItem(@FieldMap HashMap<String, Object> params);

}
