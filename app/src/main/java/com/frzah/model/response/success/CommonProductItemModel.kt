package com.frzah.model.response.success

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class CommonProductItemModel {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("seller_id")
    @Expose
    var sellerId: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("images")
    @Expose
    var images: String? = null

    @SerializedName("category_id")
    @Expose
    var categoryId: String? = null

    @SerializedName("mrp")
    @Expose
    var mrp: String? = null

    @SerializedName("sp")
    @Expose
    var sp: Int? = null

    @SerializedName("rating")
    @Expose
    var rating:String?=null

    @SerializedName("review")
    @Expose
    var review:List<ReviewModel>?=null


    @SerializedName("discount")
    @Expose
    var discount: String? = null

    @SerializedName("ships_in")
    @Expose
    var shipsIn: String? = null

    @SerializedName("favourite")
    @Expose
    var favourite: String? = null

    @SerializedName("havecart")
    @Expose
    var havecart: String? = null

}