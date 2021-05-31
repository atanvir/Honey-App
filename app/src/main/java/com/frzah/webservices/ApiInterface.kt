package com.e.carty.webservices

import com.frzah.model.request.AddressModel
import com.frzah.model.request.CommonListModel
import com.frzah.model.request.CommonModel
import com.frzah.model.request.ReviewModel
import com.frzah.utils.ParamEnum
import com.frzah.webservices.ApiConstant
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST(ApiConstant.HOME_PRODUCT)
    fun homeProducts(@Field("token") token: String,
                     @Field("latitude") latitude: Double,
                     @Field("longitude") longitude: Double): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.SEARCH)
    fun search(@Field("token") token: String,
                @Field("search_key") search_key:String,
                @Field("type") type:String,
                @Field("sort_by") sort_by:String,
                @Field("price_low") price_low:String,
                @Field("price_high") price_high:String,
                @Field("rating") rating:String) : Observable<CommonModel>


    @FormUrlEncoded
    @POST(ApiConstant.STORE_DETAIL)
    fun storeDetail(@Field("store_id") storeId:String,
                    @Field("token") token: String) : Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.PRODUCT_DETAIL)
    fun productDetail(@Field("product_id") productId:String,
                      @Field("token") token: String) : Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.PRODUCT_TYPE)
    fun productsType(@Field("store_id") storeId: String,
                     @Field("type") type: String?,
                     @Field("token") token: String,
                     @Field("cart_id") cart_id: String,
                     @Field("quantity") quantity: String): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.VERIFY_PHONE) fun verfiyPhone(@Field("phone") phone: String,
                                                    @Field("deviceToken") deviceToken: String,
                                                    @Field("deviceType") deviceType: String= ParamEnum.ANDROID.theValue() as String,
                                                    @Field("cartids") product_id: String,
                                                    @Field("sellers") seller_id: String,
                                                    @Field("quantity") quantity: String): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.SING_UP) fun signup(@Field("phone") phone: String,
                                        @Field("phone_code") phone_code: String,
                                        @Field("deviceToken") deviceToken: String,
                                        @Field("deviceType") deviceType: String= ParamEnum.ANDROID.theValue() as String,
                                        @Field("cartids") product_id: String,
                                        @Field("sellers") seller_id: String,
                                        @Field("quantity") quantity: String): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.ADD_TO_CART)
    fun addtocart(@Field("product_id") product_id: String,
                  @Field("seller_id") seller_id:String,
                  @Field("cart_empty") cart_empty:String,
                  @Field("token")  token:String) : Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.REMOVE_TO_CART)
    fun removetocart(@Field("product_id") productId:String,
                     @Field("token") token: String) : Observable<CommonModel>
    @FormUrlEncoded
    @POST(ApiConstant.UPDATE_CART)
    fun updatecart(@Field("product_id") productId:String,
                   @Field("token") token: String,
                   @Field("quantity") quantity:Int) : Observable<CommonModel>


    @FormUrlEncoded
    @POST(ApiConstant.CART_LIST)
    fun cartlist(@Field("token") token:String,
                 @Field("cart_id") cart_id: String,
                 @Field("quantity") quantity:String) : Single<CommonModel>


    @FormUrlEncoded
    @POST(ApiConstant.ADD_TO_WISHLIST)
    fun addtowish(@Field("token") token: String,
                  @Field("id") id: String,
                  @Field("type") type:String) : Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.USER_PROFILE)
    fun userprofile(@Field("token") token: String) : Observable<CommonModel>

    @Multipart
    @POST(ApiConstant.EDIT_PROFILE)
    fun userEditProfile(@Part image: MultipartBody.Part?,
                        @PartMap requestBody: Map<String,@JvmSuppressWildcards RequestBody>) : Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.FAVOURITE_PRODUCTS_LIST)
    fun favList(@Field("token") token:String,
                @Field("type") type: String) : Observable<CommonModel>

    @GET(ApiConstant.FILTER_DATA)
    fun categorylist() : Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.ALL_ADDRESS)
    fun useraddress(@Field("token") token: String): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.DEFAULT_ADDRESS)
    fun defaultAddress(@Field ("token") token: String) : Observable<CommonModel>

    @POST(ApiConstant.USER_ADDRESS)
    fun userAddAddress(@Body model:AddressModel): Observable<CommonModel>

    @POST(ApiConstant.EDIT_ADDRESS)
    fun userEditddress(@Body model:AddressModel): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.REMOVE_ADDRESS)
    fun removeAddress(@Field("address_id") addressId: String,
                      @Field("token") token: String): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.PLACE_ORDER)
    fun placeOrder(@Field("token") token: String,
                   @Field("payment_type") paymentType: String,
                   @Field("transcation_id") transcation_id: String,
                   @Field("coupon_code") coupon_code: String,
                   @Field("shipping_charges") shipping_charges: String,
                   @Field("address_id") address_id: String): Observable<CommonModel>

    @GET(ApiConstant.ALL_REVIEWS)
    fun allReviews(@Path("type") type:String,@Path("id") id:String): Observable<ReviewModel>

    @FormUrlEncoded
    @POST(ApiConstant.ADD_REVIEW)
    fun addReview(@Field("token") token: String,
                  @Field("review") review:String,
                  @Field("type") type:String,
                  @Field("product_id") product_id:String): Observable<CommonListModel>

    @Multipart
    @POST(ApiConstant.CONTACT_US)
    fun contactUs(@Part image: MultipartBody.Part,
                  @PartMap data: Map<String,@JvmSuppressWildcards RequestBody>): Observable<CommonModel>

    @GET(ApiConstant.FAQs)
    fun faq(): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.NOTIFICATION_LIST)
    fun notficationlist(@Field("token") token: String): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.LOGOUT)
    fun logout(@Field("token") token: String): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.UPCOMING_ORDER)
    fun upcomingOrder(@Field("token") token: String): Observable<CommonListModel>

    @FormUrlEncoded
    @POST(ApiConstant.PAST_ORDER)
    fun pastOrder(@Field("token") token: String) : Observable<CommonListModel>

    @GET(ApiConstant.GOOGLE_DIRECTION_API)
    fun getDirectionApi(@Query("origin") origin:String,
                        @Query("destination") destination:String,
                        @Query("key") key:String=ParamEnum.GOOGLE_MAP_KEY.theValue() as String): Observable<CommonModel>

    @GET(ApiConstant.COUPON_LIST)
    fun coupons(): Observable<CommonListModel>

    @FormUrlEncoded
    @POST(ApiConstant.APPLY_COUPON)
    fun applyCoupon(@Field("token") token: String,
                    @Field("code") couponCode: String): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.CANCEL_COUPON)
    fun removeCoupon(@Field("token") token: String): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.ORDER_DETAILS)
    fun orderDetail(@Field("token") token: String,
                    @Field("order_id") order_id: String): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.RE_ORDER)
    fun reorder(@Field("token") token:String,
                @Field("order_id") order_id:String) : Observable<CommonListModel>

    @FormUrlEncoded
    @POST(ApiConstant.CANCEL_ORDER)
    fun cancelOrder(@Field("token") token:String,
                    @Field("order_id") order_id:String) : Observable<CommonListModel>

    @FormUrlEncoded
    @POST(ApiConstant.ORDER_RATING)
    fun orderRating(@Field("token") token: String,
                    @Field("order_id") order_id: String,
                    @Field("rate_on_product") rate_on_product: String,
                    @Field("rate_on_seller") rate_on_seller: String,
                    @Field("comment") comment: String): Observable<CommonListModel>

    @FormUrlEncoded
    @POST(ApiConstant.HOME_FILTER)
    fun homesearch(@Field("token") token: String,
                   @Field("latitude") latitude: String,
                   @Field("longitude") longitude: String,
                   @Field("rating") rating: String,
                   @Field("type") type:String,
                   @Field("delivery_day") delivery_day: String,
                   @Field("price_low") price_low: String,
                   @Field("price_high")price_high: String): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.OFFER_DETAIL)
    fun offerDetailApi(@Field("token") token: String,
                       @Field("offer_id") offerId: String): Observable<CommonModel>

    @FormUrlEncoded
    @POST(ApiConstant.NOTIFICATION_COUNT)
    fun notificationToken(@Field("token")token: String): Observable<CommonModel>

}