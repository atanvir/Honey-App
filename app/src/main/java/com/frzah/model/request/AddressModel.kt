package com.frzah.model.request

data class AddressModel(val address_id: String,
                        val token:String,
                        val name: String,
                        val phone: String,
                        val address:String,
                        val street:String,
                        val city:String,
                        val state:String,
                        var type:String,
                        val remark:String,
                        val country:String,
                        val latitude:Double,
                        val longitude:Double,
                        val phone_code:String,
                        val pin_code:String)
