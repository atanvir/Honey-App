package com.frzah.model.request

import com.frzah.model.response.success.ResponseBean
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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