package com.frzah.model.response.success

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class CartListModel {
    @SerializedName("seller_id")
    @Expose
    var sellerId: String? = null

    @SerializedName("product_id")
    @Expose
    var productId: Int? = null

    @SerializedName("sold_by")
    @Expose
    var soldBy: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("name",alternate = arrayOf("product_name"))
    @Expose
    var name: String? = null

    @SerializedName("total_items")
    @Expose
    var totalItems: String? = null

    @SerializedName("mrp")
    @Expose
    var mrp: String? = null

    @SerializedName("sp",alternate = arrayOf("price"))
    @Expose
    var sp: String? = null

    @SerializedName("percentage")
    @Expose
    var percentage: String? = null

    @SerializedName("quantity")
    @Expose
    var quantity: String? = null

    @SerializedName("total_price")
    @Expose
    var totalPrice: Int? = null

    @SerializedName("discount_coupon",alternate = arrayOf("discount"))
    @Expose
    var discountCoupon: String? = null

    @SerializedName("coupon_code")
    @Expose
    var couponCode: String? = null

    @SerializedName("sub_total")
    @Expose
    val sub_toal: Long?=null

    @SerializedName("tax")
    @Expose
    val tax: Long?=null

    @SerializedName("discounted_amount")
    @Expose
    val discounted_amount: Long?=null

    @SerializedName("total")
    @Expose
    val total: Long?=null

    @SerializedName("category_id",alternate = arrayOf("category"))
    @Expose
    val category_id:String?=null

    @SerializedName("latitude")
    @Expose
    val latitude:String?=null

    @SerializedName("longitude")
    @Expose
    val longitude:String?=null
}