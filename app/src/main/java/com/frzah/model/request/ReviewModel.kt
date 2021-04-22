package com.frzah.model.request

import com.frzah.model.response.success.UserModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewModel {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("response")
    @Expose
    var response: List<ResponseBean>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    public class ResponseBean{
        @SerializedName("review")
        @Expose
        var review:String?= null

        @SerializedName("user")
        @Expose
        var user:UserModel?=null

        @SerializedName("created_at")
        @Expose
        var created_at:String?=null
    }
}