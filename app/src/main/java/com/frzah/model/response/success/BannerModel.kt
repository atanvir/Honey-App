package com.frzah.model.response.success

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BannerModel {

    @SerializedName("id")
    @Expose
    var id:String?=null

    @SerializedName("type")
    @Expose
    var type:String?=null

    @SerializedName("image")
    @Expose
    var image:String?=null

    @SerializedName("category_id")
    @Expose
    var category_id:String?=null
}