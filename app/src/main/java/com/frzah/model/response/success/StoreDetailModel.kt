package com.frzah.model.response.success

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class StoreDetailModel {

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("delivery_time")
    @Expose
    var deliveryTime: Any? = null

    @SerializedName("shipping")
    @Expose
    var shipping: String? = null

    @SerializedName("rating")
    @Expose
    var rating: Int? = null


}
