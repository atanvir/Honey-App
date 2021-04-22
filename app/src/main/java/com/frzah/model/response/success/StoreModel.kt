package com.frzah.model.response.success

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class StoreModel {

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("commission")
    @Expose
    var commission: Int? = null

    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("pincode")
    @Expose
    var pincode: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("delivery_time")
    @Expose
    var deliveryTime: String? = null

    @SerializedName("shipping")
    @Expose
    var shipping: String? = null

    @SerializedName("rating")
    @Expose
    var rating: Int? = null

    @SerializedName("review")
    @Expose
    var review:List<ReviewModel>?=null

    @SerializedName("type")
    @Expose
    var type: List<String>? = null

    @SerializedName("favourite")
    @Expose
    var favourite: String?=null

    @SerializedName("cover_image")
    @Expose
    var cover_image: String?=null


}
