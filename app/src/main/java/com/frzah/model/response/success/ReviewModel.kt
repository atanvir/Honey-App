package com.frzah.model.response.success

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewModel {
    @SerializedName("description")
    @Expose
    private val description:String?=null

    @SerializedName("name")
    @Expose
    private val name:String?=null

    @SerializedName("image")
    @Expose
    private val image:String?=null

    @SerializedName("created_at")
    @Expose
    private val created_at:String?=null


}