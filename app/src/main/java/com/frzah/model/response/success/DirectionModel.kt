package com.frzah.model.response.success

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DirectionModel {
    @SerializedName("distance")
    @Expose
    var distance:Distance?=null


    public class Distance{
       @SerializedName("text")
       @Expose
       var text:String?=null
    }

}
