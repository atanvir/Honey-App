package com.frzah.model.response.success

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ProductDetailModel() :Parcelable{
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("sku")
    @Expose
    var sku: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("product_search")
    @Expose
    var productSearch: Any? = null

    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    @SerializedName("sp")
    @Expose
    var sp: String? = null

    @SerializedName("commision")
    @Expose
    var commision: Int? = null

    @SerializedName("tax")
    @Expose
    var tax: Int? = null

    @SerializedName("quantity",alternate = arrayOf("qty"))
    @Expose
    var quantity: String? = null

    @SerializedName("mrp")
    @Expose
    var mrp: String? = null

    @SerializedName("discount")
    @Expose
    var discount: String? = null

    @SerializedName("category_id")
    @Expose
    var categoryId: String? = null

    @SerializedName("user_type")
    @Expose
    var userType: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("weight")
    @Expose
    var weight: String? = null

    @SerializedName("ships_in")
    @Expose
    var shipsIn: String? = null

    @SerializedName("images",alternate = arrayOf("image"))
    @Expose
    var images: String? = null

    @SerializedName("size")
    @Expose
    var size: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("deleteStatus")
    @Expose
    var deleteStatus: Int? = null

    @SerializedName("wbn")
    @Expose
    var wbn: Any? = null

    @SerializedName("payment_mode")
    @Expose
    var paymentMode: Int? = null

    @SerializedName("rating")
    @Expose
    var rating: Int? = null

    @SerializedName("rating_count")
    @Expose
    var rating_count:Int?=null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("delivery_time")
    @Expose
    var delivery_time: String?=null

    @SerializedName("type")
    @Expose
    var type:List<String>?=null

   @SerializedName("seller_id")
    @Expose
    var seller_id:String?=null

    @SerializedName("favourite")
    @Expose
    var favourite:String?=null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        sku = parcel.readString()
        name = parcel.readString()
        userId = parcel.readString()
        sp = parcel.readString()
        commision = parcel.readValue(Int::class.java.classLoader) as? Int
        tax = parcel.readValue(Int::class.java.classLoader) as? Int
        quantity = parcel.readString()
        mrp = parcel.readString()
        discount = parcel.readString()
        categoryId = parcel.readString()
        userType = parcel.readString()
        description = parcel.readString()
        weight = parcel.readString()
        shipsIn = parcel.readString()
        images = parcel.readString()
        size = parcel.readString()
        status = parcel.readString()
        deleteStatus = parcel.readValue(Int::class.java.classLoader) as? Int
        paymentMode = parcel.readValue(Int::class.java.classLoader) as? Int
        rating = parcel.readValue(Int::class.java.classLoader) as? Int
        rating_count = parcel.readValue(Int::class.java.classLoader) as? Int
        createdAt = parcel.readString()
        updatedAt = parcel.readString()
        delivery_time = parcel.readString()
        type = parcel.createStringArrayList()
        seller_id = parcel.readString()
        favourite = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(sku)
        parcel.writeString(name)
        parcel.writeString(userId)
        parcel.writeString(sp)
        parcel.writeValue(commision)
        parcel.writeValue(tax)
        parcel.writeString(quantity)
        parcel.writeString(mrp)
        parcel.writeString(discount)
        parcel.writeString(categoryId)
        parcel.writeString(userType)
        parcel.writeString(description)
        parcel.writeString(weight)
        parcel.writeString(shipsIn)
        parcel.writeString(images)
        parcel.writeString(size)
        parcel.writeString(status)
        parcel.writeValue(deleteStatus)
        parcel.writeValue(paymentMode)
        parcel.writeValue(rating)
        parcel.writeValue(rating_count)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
        parcel.writeString(delivery_time)
        parcel.writeStringList(type)
        parcel.writeString(seller_id)
        parcel.writeString(favourite)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductDetailModel> {
        override fun createFromParcel(parcel: Parcel): ProductDetailModel {
            return ProductDetailModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductDetailModel?> {
            return arrayOfNulls(size)
        }
    }

}
