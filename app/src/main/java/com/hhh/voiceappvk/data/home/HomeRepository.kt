package com.hhh.voiceappvk.data.home

import android.util.Log
import com.hhh.voiceappvk.data.home.HomeDao
import com.hhh.voiceappvk.util.UiState
import com.vk.api.sdk.VK
import com.vk.sdk.api.docs.DocsService
import com.vk.sdk.api.docs.dto.DocsDoc
import com.vk.sdk.api.docs.dto.TypeParam
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class HomeRepository @Inject constructor() : HomeDao {
    override fun getFiles(result: (UiState<List<DocsDoc>>) -> Unit) {
        val dispose = Observable.fromCallable {
            VK.executeSync(DocsService().docsGet(type = TypeParam.AUDIO, ownerId = VK.getUserId()))
        }
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("Docs result", "${it.count}: ${it.items}")
                result.invoke(UiState.Success(it.items))
            }, {
                Log.e("Docs result", it.localizedMessage!!.toString())
                result.invoke(UiState.Error(it.localizedMessage))
            })


        if (dispose.isDisposed) {
            dispose.dispose()
        }
    }
}