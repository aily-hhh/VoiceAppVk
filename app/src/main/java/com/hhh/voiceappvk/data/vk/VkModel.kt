package com.hhh.voiceappvk.data.vk

import android.util.Log
import com.vk.api.sdk.VK
import com.vk.sdk.api.docs.DocsService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request

@kotlinx.serialization.Serializable
data class VkModel(val file: String)

//dispose = Observable.fromCallable {
//    VK.executeSync(DocsService().docsGetUploadServer())
//}
//.subscribeOn(Schedulers.computation())
//.observeOn(AndroidSchedulers.mainThread())
//.subscribe({
//
//    val formBody = MultipartBody.Builder()
//        .setType(MultipartBody.FORM)
//        .addFormDataPart("file", audioFile!!.nameWithoutExtension)
//        .build()
//
//    val call = OkHttpClient().newCall(
//        Request.Builder()
//            .post(formBody)
//            .url(it.uploadUrl)
//            .build())
//
//    val body = call.execute().body?.string()
//    Log.d("Docs result", "body: $body")
//    val vkFile = Json.decodeFromString<VkModel>(body!!).file
//    VK.executeSync(DocsService().docsSave(vkFile, title = "myVkFile"))
//
//
//}, {
//    Log.e("Docs result", it.localizedMessage?.toString() ?: "error")
//})
