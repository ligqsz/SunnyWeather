package com.ligq.sunnyweather.logic

import androidx.lifecycle.liveData
import com.ligq.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

/**
 * 仓库类，用于获取并处理数据
 */
object Repository {
    /**
     * 搜索城市方法，使用liveData协程
     */
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        //获取数据结果
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlace(query)
            if ("ok" == placeResponse.status) {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        //发送结果给观察者，如activity，fragment
        emit(result)
    }
}