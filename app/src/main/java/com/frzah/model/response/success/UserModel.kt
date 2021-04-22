package com.frzah.model.response.success

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class UserModel {

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("phone_code")
    @Expose
    var phoneCode: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("dob")
    @Expose
    var dob: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("notify_status")
    @Expose
    var notifyStatus: String? = null

    @SerializedName("social_id")
    @Expose
    var socialId: String? = null

    @SerializedName("social_type")
    @Expose
    var socialType: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("referal_code")
    @Expose
    var referalCode: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null


}
