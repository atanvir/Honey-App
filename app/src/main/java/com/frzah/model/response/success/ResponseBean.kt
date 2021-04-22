package com.frzah.model.response.success

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseBean {

    @SerializedName("cart_details")
    @Expose
    var cart_details:CartDetailsModel?=null

    @SerializedName("count")
    @Expose
    var count:String?=null

    @SerializedName("legs")
    @Expose
    var legs:List<DirectionModel>?=null

    @SerializedName("product_list", alternate = arrayOf("products","product"))
    @Expose
    var productList: MutableList<ProductDetailModel>? = null

    @SerializedName("review")
    @Expose
    var review:List<ReviewModel>?=null


    @SerializedName("store_list", alternate = arrayOf("stores"))
    @Expose
    var storeList: MutableList<ProductDetailModel>? = null

    @SerializedName("cart_list")
    @Expose
    val cart_list: List<CartListModel>?=null

    @SerializedName("Cart_amount")
    @Expose
    val Cart_amount: CartListModel?=null

    @SerializedName("total_items_in_cart")
    @Expose
    val total_items_in_cart: Long?=null

    @SerializedName("user")
    @Expose
    var user: UserModel? = null

    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("id")
    @Expose
    var id: Long? = null

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

    @SerializedName("offer_price")
    @Expose
    var offer_price: String? = null

    @SerializedName("commision")
    @Expose
    var commision: Int? = null

    @SerializedName("tax")
    @Expose
    var tax: String? = null

    @SerializedName("total_discount")
    @Expose
    var total_discount: String? = null

    @SerializedName("shipping_charge")
    @Expose
    var shipping_charge: String? = null


    @SerializedName("tracking_id")
    @Expose
    var tracking_id: String? = null

    @SerializedName("tracking_url")
    @Expose
    var tracking_url: String? = null

    @SerializedName("quantity")
    @Expose
    var quantity: Long? = null

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

    @SerializedName("images", alternate = arrayOf("image"))
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

    @SerializedName("total_rating")
    @Expose
    var total_rating:String?=null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("seller_id")
    @Expose
    var sellerId: String? = null

    @SerializedName("attributes")
    @Expose
    var attributes: List<AttributeModel>? = null

    @SerializedName("favourite")
    @Expose
    var favourite: String? = null

    @SerializedName("banner")
    @Expose
    val banner: List<BannerModel>? = null

    @SerializedName("allstores")
    @Expose
    val allstores: List<CommonShopsItemModel>? = null


    @SerializedName("latest")
    @Expose
    val latest: List<CommonShopsItemModel>? = null

    @SerializedName("popular")
    @Expose
    val popular: List<CommonShopsItemModel>? = null

    @SerializedName("offers")
    @Expose
    val offers: List<CommonShopsItemModel>? = null

    @SerializedName("distance")
    @Expose
    val distance: List<CommonShopsItemModel>? = null

    @SerializedName("store")
    @Expose
    var storeModel: StoreModel? = null

    @SerializedName("featured")
    @Expose
    var featured: List<CommonProductItemModel>? = null

    @SerializedName("list")
    @Expose
    var list:List<CommonProductItemModel>? = null

    @SerializedName("havecart",alternate = arrayOf("haveCart"))
    @Expose
    var havecart: String?=null

    @SerializedName("haveQTY")
    @Expose
    val haveQTY:Int?=null

    @SerializedName("phone")
    @Expose
    val phone: String?=null

    @SerializedName("address")
    @Expose
    val address: String?=null

    @SerializedName("street")
    @Expose
    val street: String?=null

    @SerializedName("state")
    @Expose
    val state: String?=null

    @SerializedName("city")
    @Expose
    val city: String?=null

    @SerializedName("type")
    @Expose
    val type: String?=null

    @SerializedName("remark")
    @Expose
    val remark: String?=null

    @SerializedName("latitude")
    @Expose
    val latitude: Double?=null

    @SerializedName("longitude")
    @Expose
    val longitude: Double?=null

    @SerializedName("country")
    @Expose
    val country: String?=null


    @SerializedName("phone_code")
    @Expose
    val phone_code: String?=null

    @SerializedName("pin_code")
    @Expose
    val pin_code: String?=null

    @SerializedName("question")
    @Expose
    val question:String?=null

    @SerializedName("ans")
    @Expose
    val ans:String?=null

    @SerializedName("title")
    @Expose
    val title:String?=null

    @SerializedName("content")
    @Expose
    val content:String?=null

    @SerializedName("order_id")
    @Expose
    val order_id:String?=null

    @SerializedName("order_number")
    @Expose
    val order_number:String?=null

    @SerializedName("seller_image")
    @Expose
    val seller_image:String?=null

    @SerializedName("seller_name")
    @Expose
    val seller_name:String?=null

    @SerializedName("seller_address")
    @Expose
    val seller_address:String?=null

    @SerializedName("dispatch_at")
    @Expose
    val dispatch_at:String?=null

    @SerializedName("order_date")
    @Expose
    val order_date:String?=null

    @SerializedName("payment_type")
    @Expose
    val payment_type:String?=null

    @SerializedName("amount")
    @Expose
    val amount:String?=null

    @SerializedName("item_count")
    @Expose
    var item_count:Long?=null

    @SerializedName("coupon_code")
    @Expose
    val coupon_code:String?=null

    @SerializedName("discount_type")
    @Expose
    val discount_type:String?=null

    @SerializedName("expiry_date")
    @Expose
    val expiry_date:String?=null

    @SerializedName("coupon_id")
    @Expose
    val coupon_id:String?=null

    @SerializedName("min_price")
    @Expose
    val min_price:String?=null


    @SerializedName("order_items")
    @Expose
    var orderItems: List<CartListModel>? = null

    @SerializedName("customer_name")
    @Expose
    var customerName: String? = null

    @SerializedName("customer_address")
    @Expose
    var customerAddress: String? = null

    @SerializedName("customer_state")
    @Expose
    var customerState: String? = null

    @SerializedName("customer_city")
    @Expose
    var customerCity: String? = null

    @SerializedName("customer_country")
    @Expose
    var customerCountry: String? = null

    @SerializedName("customer_phone")
    @Expose
    var customerPhone: String? = null

    @SerializedName("total_amount")
    @Expose
    var totalAmount: String? = null

    @SerializedName("final_amount")
    @Expose
    var finalAmount: String? = null


}