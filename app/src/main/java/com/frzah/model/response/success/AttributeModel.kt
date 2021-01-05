package com.frzah.model.response.success

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

 class AttributeModel {
    @SerializedName("labal")
    @Expose
    var labal: String? = null

    @SerializedName("text")
    @Expose
    var text: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null


 }
