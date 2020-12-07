package com.honey.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.honey.model.response.success.ResponseBean

class CommonListModel {

    @SerializedName("status")
    @Expose
    val status: String? = null

    @SerializedName("response")
    @Expose
    val response: List<ResponseBean>? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

}