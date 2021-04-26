package com.ligq.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ligq.sunnyweather.logic.Repository
import com.ligq.sunnyweather.logic.model.Place

/**
 * 城市搜索的ViewModel类
 */
class PlaceViewModel : ViewModel() {
    /**
     * 城市搜索数据
     */
    private val searchLiveData = MutableLiveData<String>()

    /**
     * 网络请求成功后，数据发生变化，进而通知观察者进行数据刷新
     */
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    /**
     * 存储已经获取到的城市
     */
    val placeList = ArrayList<Place>()

    /**
     * 改变搜索城市，进而发起网络请求，详见@link{PlaceViewModel#placeLiveData}
     */
    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }
}