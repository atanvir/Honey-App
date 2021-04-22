package com.frzah.utils

enum class ParamEnum {
    SUCCESS("SUCCESS"),
    STORE_ID( "store_id"),
    FAILURE("FAILURE"),
    ANDROID("android"),
    PRODUCT("product"),
    STORE("store"),
    CITY("city"),
    STATE("state"),
    COUNTRY("country"),
    PIN_CODE("pincode"),
    ADDRESS("address"),
    LATITUDE("latitude"),
    LONGITUDE("longitude"),
    HOME("home"),
    OFFICE("office"),
    COD("COD"),
    ONLINE("online"),
    TYPE("type"),
    SORT_BY("sort_by"),
    PRICE_LOW("price_low"),
    PRICE_HIGH("price_high"),
    RATING("rating"),
    PRODUCT_ID("product_id"),
    SELLER_ID("seller_id"),
    QUANTITY("quantity"),
    PRICE("price"),
    PERCENT("percent"),
    PRODUCT_TYPE("0"),
    STORE_TYPE("1"),
    ORDER_ID("order_id"),
//    API_BASE_URL("https://mobuloustech.com/honey_app/api/"),
    API_BASE_URL("http://54.152.130.226/honey_app/api/"),
    BASE_URL("http://54.152.130.226/"),
//    GOOGLE_MAP_KEY("AIzaSyBIcOPG6bi5aIgM-99JWsF_boy5_t9QjGE"),
    GOOGLE_MAP_KEY("AIzaSyDgPSrkLQMUmrfhth86Naz6Es42TDPZWrA"),
    TELR_STORE_ID("24967"),
    TELR_EMAIL("almoqtan3@hotmail.com"),
    TELR_API_KEY("vctdk-j3x6G^hcFs"),
    GOOGLE_MAP_BASE_URL("https://maps.google.com/maps/api/directions/"),
    LOCALITY("locality");


    private val value: Any?

    constructor(value: Any?) {
        this.value = value
    }

    constructor() {
        value = null
    }

    fun theValue(): Any? {
        return value
    }
}