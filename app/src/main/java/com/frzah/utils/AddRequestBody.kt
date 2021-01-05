package com.frzah.utils

import com.frzah.model.request.ContactUsModel
import com.frzah.model.request.UserProfileModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*


class AddRequestBody<T>(data: T) {
    private val mediaType = "text/plain".toMediaTypeOrNull()
    private val requestBodyMap: MutableMap<String, RequestBody> = HashMap()

    val body: Map<String, RequestBody> get() = requestBodyMap

    init {
        if (data is UserProfileModel) {
            requestBodyMap.put("token",(data as UserProfileModel).token.toRequestBody(mediaType))
            requestBodyMap.put("phone",(data as UserProfileModel).phone.toRequestBody(mediaType))
            requestBodyMap.put("name",(data as UserProfileModel).name.toRequestBody(mediaType))
            requestBodyMap.put("email",(data as UserProfileModel).email.toRequestBody(mediaType))
        }else if(data is ContactUsModel)
        {
            requestBodyMap.put("reason",(data as ContactUsModel).reason.toRequestBody(mediaType))
            requestBodyMap.put("query",(data as ContactUsModel).query.toRequestBody(mediaType))
        }
    }
}
