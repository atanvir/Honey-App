package com.honey.model.response.success

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

class CommonShopsItemModel(
    @SerializedName("id")
    @Expose var id: Int?,
    @SerializedName("name")
    @Expose
    var name: String?,
    @SerializedName("image")
    @Expose
    var image: String?,
    @SerializedName("delivery_time")
    @Expose
    var deliveryTime: String?,
    @SerializedName("rating")
    @Expose
    var rating: Int?,
    @SerializedName("rating_count")
    @Expose
    var ratingCount: Int?,

    @SerializedName("favourite")
    @Expose
    var favourite: String?)
{

    @SerializedName("seller_id")
    @Expose
    val seller_id: String? = null

    @SerializedName("description")
    @Expose
    val description: String? = null

    @SerializedName("mrp")
    @Expose
    val mrp: String? = null

    @SerializedName("offer_price")
    @Expose
    val offer_price:String?=null


    @SerializedName("email")
    @Expose
    var email: String? = null


    @SerializedName("shipping")
    @Expose
    var shipping: String? = null

}