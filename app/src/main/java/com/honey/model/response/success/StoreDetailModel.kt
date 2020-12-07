package com.honey.model.response.success

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize


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
