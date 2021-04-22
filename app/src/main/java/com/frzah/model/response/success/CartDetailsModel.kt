package com.frzah.model.response.success

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CartDetailsModel {
    @SerializedName("sub_total")
    @Expose
    val sub_total:Double?=null

    @SerializedName("tax")
    @Expose
    val tax:Double?=null

    @SerializedName("discounted_amount")
    @Expose
    val discounted_amount:Double?=null


    @SerializedName("total")
    @Expose
    val total:Double?=null

    @SerializedName("total_itmes")
    @Expose
    val total_itmes:Double?=null

}