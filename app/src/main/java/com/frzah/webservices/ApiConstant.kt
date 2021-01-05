package com.frzah.webservices

import java.io.Serializable

class ApiConstant : Serializable {
    companion object {
        // Project Api's
        const val SEARCH= "search"
        const val HOME_PRODUCT="home_product"
        const val STORE_DETAIL="store_detail"
        const val PRODUCT_DETAIL="product_detail"
        const val PRODUCT_TYPE="products_type"
        const val VERIFY_PHONE="verify_phone"
        const val SING_UP="signup"
        const val ADD_TO_CART="addtocart"
        const val REMOVE_TO_CART="removetocart"
        const val UPDATE_CART="updatecart"
        const val CART_LIST="cart_list"
        const val ADD_TO_WISHLIST="addtowish"
        const val USER_PROFILE="user_profile"
        const val EDIT_PROFILE="user_edit_profile"
        const val FAVOURITE_PRODUCTS_LIST="wishlistproduct"
        const val FILTER_DATA="categorylist"
        const val ALL_ADDRESS="user_address"
        const val DEFAULT_ADDRESS="default_address"
        const val USER_ADDRESS="user_add_address"
        const val REMOVE_ADDRESS="user_remove_address"
        const val EDIT_ADDRESS="user_edit_address"
        const val PLACE_ORDER="place_order"
        const val ALL_REVIEWS="review_list/{type}/{id}"
        const val ADD_REVIEW="add_review"
        const val CONTACT_US="contact-us"
        const val FAQs="faq"
        const val ABOUT_US="about-us"
        const val TERM_AND_CONDITION="tandc"
        const val PRIVACY_POLICY="privacy-policy"
        const val NOTIFICATION_LIST="notficationlist"
        const val LOGOUT="logout"
        const val UPCOMING_ORDER="upcoming_orders"
        const val PAST_ORDER="past_orders"
        const val COUPON_LIST="coupons"
        const val APPLY_COUPON="apply_coupon"
        const val CANCEL_COUPON="cancel_coupon"
        const val ORDER_DETAILS="order_detail"
        const val CANCEL_ORDER="cancel_order"
        const val RE_ORDER="reorder"
        const val ORDER_RATING="order_rating"
        const val HOME_FILTER="homesearch"
        const val OFFER_DETAIL="offer_detail"
        const val NOTIFICATION_COUNT="notiication_count"

        // Google Map Api
        const val GOOGLE_DIRECTION_API="json"
    }
}