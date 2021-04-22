package com.frzah.model.request

import com.frzah.model.response.success.ResponseBean
import com.frzah.model.response.success.UserModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CommonModel{

    @SerializedName("status")
    @Expose
    val status: String? = null

    @SerializedName("response")
    @Expose
    val response: ResponseBean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("requestKey")
    @Expose
    val requestKey: String? = null

    @SerializedName("signup",alternate = arrayOf("verify_phone"))
    @Expose
    val signup: ResponseBean?=null

    @SerializedName("addtocart")
    @Expose
    val addtocart: ResponseBean?=null

    @SerializedName("cart_list")
    @Expose
    val cart_list:ResponseBean?=null

    @SerializedName("user_profile",alternate = arrayOf("user_edit_profile"))
    @Expose
    val user_profile: UserModel?=null

    @SerializedName("wishlistproduct")
    @Expose
    var wishlistproduct: ResponseBean? = null

    @SerializedName("categorylist")
    @Expose
    var categorylist: List<ResponseBean>?=null

    @SerializedName("user_address")
    @Expose
    var user_address: MutableList<ResponseBean>?=null

    @SerializedName("default_address")
    @Expose
    var default_address: ResponseBean?=null

    @SerializedName("faq")
    @Expose
    var faq:List<ResponseBean>?=null

    @SerializedName("notficationlist")
    @Expose
    var notficationlist:List<ResponseBean>?=null


    @SerializedName("routes")
    @Expose
    var routes:List<ResponseBean>?=null

    @SerializedName("offer_detail")
    @Expose
    var offer_detail:ResponseBean?=null


 }
