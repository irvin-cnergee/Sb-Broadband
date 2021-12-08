package developer.app.interface_;

import com.google.gson.JsonObject;

import java.util.List;

import cnergee.sbbroadband.adapters.PackageHistoryData;
import cnergee.sbbroadband.obj.UsageData;
import cnergee.sbbroadband.retroObj.ActiveFlagData;
import cnergee.sbbroadband.retroObj.CommonResolutionData;
import cnergee.sbbroadband.retroObj.ComplaintData;
import cnergee.sbbroadband.retroObj.CurrentPckgRateData;
import cnergee.sbbroadband.retroObj.CustomerCareData;
import cnergee.sbbroadband.retroObj.DuesData;
import cnergee.sbbroadband.retroObj.EntityDatum;
import cnergee.sbbroadband.retroObj.HelpData;
import cnergee.sbbroadband.retroObj.LanguageDatum;
import cnergee.sbbroadband.retroObj.MobileLoginData;
import cnergee.sbbroadband.retroObj.NotificationData;
import cnergee.sbbroadband.retroObj.NullResolutionDatum;
import cnergee.sbbroadband.retroObj.OnlineRenewResponse;
import cnergee.sbbroadband.retroObj.PackageList;
import cnergee.sbbroadband.retroObj.ProfileData;
import cnergee.sbbroadband.retroObj.ResolutionResponse;
import cnergee.sbbroadband.retroObj.ResponseData;
import cnergee.sbbroadband.retroObj.SubscriberPckDatum;
import cnergee.sbbroadband.retroObj.TransactionData;
import cnergee.sbbroadband.retroObj.VersionResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Siddhesh on 8/4/2017.
 */

public interface AllAPIs {

    @FormUrlEncoded
    @POST("internet_usage_details")
    Call<UsageData> getUsageData(@Field("subscriberId") int subscriberId);

    @FormUrlEncoded
    @POST("get_packages_by_connection_type")
    Call<List<PackageList>> getPackagesList(@Field("subscriberId") int subscriberId,
                                            @Field("packageId") int packageId);

    @FormUrlEncoded
    @POST("get_transaction_details")
    Call<TransactionData> getTransactioHistory(@Field("subscriberId") int subscriberId,
                                               @Field("entityId") int entityId);

    @FormUrlEncoded
    @POST("profile_details")
    Call<ProfileData> getProfileDetails(@Field("subscriberId") int subscriberId,
                                        @Field("entityId") int entityId);

    @FormUrlEncoded
    @POST("update_profile_details")
    Call<ResponseData> updateProfileDetails(@Field("subscriberId") int subscriberId,
                                            @Field("entityId") int entityId,
                                            @Field("subscriber_username") String subscriber_username,
                                            @Field("first_name") String first_name,
                                            @Field("last_name") String last_name,
                                            @Field("dob") String dob,
                                            @Field("gender") String gender,
                                            @Field("contact") String contact,
                                            @Field("email") String email,
                                            @Field("address") String address);

    @FormUrlEncoded
    @POST("subscriber_package_details")
    Call<SubscriberPckDatum> getSubscriberPackages(@Field("subscriberId") int subscriberId,
                                                   @Field("entityId") int entityId);

    @FormUrlEncoded
    @POST("get_languages")
    Call<LanguageDatum> getLanguages(@Field(" ") String lang);

    @FormUrlEncoded
    @POST("get_complaint_list")
    Call<ComplaintData> getComplaints(@Field("entityId") int entityId,
                                      @Field("productId") int productId);

    @FormUrlEncoded
    @POST("launch_complaint")
    Call<ResponseData> launchResponse(@Field("subscriberId") int subscriberId,
                                      @Field("entityId") int entityId,
                                      @Field("issuer_name") String issuer_name,
                                      @Field("issuer_username") String issuer_username,
                                      @Field("areaId") int areaId,
                                      @Field("complaintId") int complaintId,
                                      @Field("desc") String desc,
                                      @Field("file") String file,
                                      @Field("createDate") String createDate,
                                      @Field("adminId") int adminId);

    @FormUrlEncoded
    @POST("mb_login")
    Call<MobileLoginData> getLoginResponse(@Field("mobileno") String mobileno,
                                           @Field("ClientAccessID") String ClientAccessID,
                                           @Field("role") String role);

    @FormUrlEncoded
    @POST("mb_login")
    Call<MobileLoginData> getEmailLoginResponse(@Field("username") String username,
                                                @Field("password") String password,
                                                @Field("ClientAccessID") String ClientAccessID,
                                                @Field("role") String role);

    @FormUrlEncoded
    @POST("mb_insert_phonedata")
    Call<ResponseData> insertPhoneDetails(@Field("subscriberId") int subscriberId,
                                          @Field("entityId") int entityId,
                                          @Field("login_type") String login_type,
                                          @Field("country_code") String country_code,
                                          @Field("phone_name") String phone_name,
                                          @Field("phone_version") String phone_version,
                                          @Field("phone_unique_id") String phone_unique_id,
                                          @Field("phone_platform") String phone_platform,
                                          @Field("app_version") String app_version,
                                          @Field("manufacturer") String manufacturer,
                                          @Field("imei") String imei,
                                          @Field("device_id") String device_id);


    @FormUrlEncoded
    @POST("verify_otp")
    Call<ResponseData> verifyOTP(@Field("subscriberId") int subscriberId,
                                 @Field("entityId") int entityId,
                                 @Field("otp") String otp);


    @FormUrlEncoded
    @POST("onlinePayment")
    Call<OnlineRenewResponse> onlineRenew(@Field("subscriberId") int subscriberId,
                                          @Field("entityId") int entityId,
                                          @Field("package_id") int package_id,
                                          @Field("renewal_mode") int renewal_mode,
                                          @Field("totalAmount") int totalAmount,
                                          @Field("currency") String currency,
                                          @Field("currentDateTime") String currentDateTime,
                                          @Field("created_by") int created_by);

    @FormUrlEncoded
    @POST("checkForUpdate")
    Call<VersionResponse> checkUpdate(@Field("category_id") int category_id,
                                      @Field("app_version") String app_version);

    @POST("selfresolution")
    Call<List<ResolutionResponse>> getSelfResolution(@Body JsonObject object);

    @POST("selfresolution")
    Call<List<NullResolutionDatum>> getNullResolution(@Body JsonObject object);

   /* @POST("selfresolution")
    Call<List<CommonResolutionData>> getCommonResolution(@Body JsonObject object);*/

    @POST("selfresolution")
    Call<CommonResolutionData> getCommonResolution(@Body JsonObject object);


    @FormUrlEncoded
    @POST("verifyAppOnSingleDevice")
    Call<ActiveFlagData> getActiveFlagResponse(@Field("entityId") int entityId,
                                               @Field("subscriberId") int subscriberId,
                                               @Field("phone_unique_id") String phone_unique_id);

    @FormUrlEncoded
    @POST("showPaymentPickup")
    Call<DuesData> getDuesData(@Field("entityId") int entityId,
                               @Field("subscriberId") int subscriberId);

    @FormUrlEncoded
    @POST("launchPickupRequest")
    Call<ResponseData> launchPickupRequest(@Field("duesId") int duesId,
                                           @Field("areaId") int areaId,
                                           @Field("comments") String comments,
                                           @Field("entityId") int entityId,
                                           @Field("requestTime") String requestTime,
                                           @Field("subscriberId") int subscriberId,
                                           @Field("status") int status);


    @FormUrlEncoded
    @POST("showNotifications")
    Call<NotificationData> getNotifications(@Field("entityId") int entityId,
                                            @Field("subscriberId") int subscriberId);

    @Multipart
    @POST("fileUploadByApp")
    Call<ResponseBody> upload(
            @Part MultipartBody.Part file, @Part("file") RequestBody name
    );


    @FormUrlEncoded
    @POST("getCustomerCareNo")
    Call<CustomerCareData> getCustomerCareData(@Field("entityId") int entityId);

    @FormUrlEncoded
    @POST("getHelpContent")
    Call<HelpData> getHelpData(@Field("entityId") int entityId);

    @FormUrlEncoded
    @POST("getTermsAndConditions")
    Call<HelpData> getTermsAndConditions(@Field("entityId") int entityId);

    @FormUrlEncoded
    @POST("getPackageHistory")
    Call<PackageHistoryData> getPackageHistory(@Field("subscriberId") int subscriberId);

    @FormUrlEncoded
    @POST("getEntityDetails")
    Call<List<EntityDatum>> getEntityData(@Field("uniqueId") String uniqueId);

    @FormUrlEncoded
    @POST("getCurrentPackageRate")
    Call<CurrentPckgRateData> getCurrentPckgRate(@Field("packageId") int packageId,
                                                 @Field("subscriberId") int subscriberId,
                                                 @Field("areaId") int areaId,
                                                 @Field("entityId") int entityId);

}
