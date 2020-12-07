package com.honey.utils

enum class ParamEnum {

    SUCCESS("SUCCESS"),
    STORE_ID("store_id"),
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
    BASE_URL("https://mobuloustech.com/honey_app/api/"),
    GOOGLE_MAP_KEY("AIzaSyBIcOPG6bi5aIgM-99JWsF_boy5_t9QjGE"),
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