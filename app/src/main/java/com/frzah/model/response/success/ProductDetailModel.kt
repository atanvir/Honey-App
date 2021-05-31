package com.frzah.model.response.success

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetailModel(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("sku")
    @Expose
    var sku: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,


    @SerializedName("user_id")
    @Expose
    var userId: String? = null,

    @SerializedName("sp")
    @Expose
    var sp: String? = null,

    @SerializedName("commision")
    @Expose
    var commision: Int? = null,

    @SerializedName("tax")
    @Expose
    var tax: Int? = null,

    @SerializedName("quantity",alternate = arrayOf("qty"))
    @Expose
    var quantity: String? = null,

    @SerializedName("mrp")
    @Expose
    var mrp: String? = null,

    @SerializedName("discount")
    @Expose
    var discount: String? = null,

    @SerializedName("category_id")
    @Expose
    var categoryId: String? = null,

    @SerializedName("user_type")
    @Expose
    var userType: String? = null,

    @SerializedName("description")
    @Expose
    var description: String? = null,

    @SerializedName("weight")
    @Expose
    var weight: String? = null,

    @SerializedName("ships_in")
    @Expose
    var shipsIn: String? = null,

    @SerializedName("images",alternate = arrayOf("image"))
    @Expose
    var images: String? = null,

    @SerializedName("size")
    @Expose
    var size: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("deleteStatus")
    @Expose
    var deleteStatus: Int? = null,


    @SerializedName("payment_mode")
    @Expose
    var paymentMode: Int? = null,

    @SerializedName("rating")
    @Expose
    var rating: Int? = null,

    @SerializedName("rating_count")
    @Expose
    var rating_count:Int?=null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,

    @SerializedName("delivery_time")
    @Expose
    var delivery_time: String?=null,

    @SerializedName("type")
    @Expose
    var type:List<TypeModel>?=null,

   @SerializedName("seller_id")
    @Expose
    var seller_id:String?=null,

    @SerializedName("favourite")
    @Expose
    var favourite:String?=null): Parcelable

