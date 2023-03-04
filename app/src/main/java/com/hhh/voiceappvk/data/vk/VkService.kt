package com.hhh.voiceappvk.data.vk

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url


interface VkService {

    @Multipart
    @POST()
    fun uploadFile(
        @Part("file") fileName: String,
        @Part("file") file: RequestBody,
        @Url() url: String
    ): Call<VkModel>
}