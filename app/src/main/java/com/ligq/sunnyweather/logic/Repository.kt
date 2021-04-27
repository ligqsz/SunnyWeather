package com.ligq.sunnyweather.logic

import androidx.lifecycle.liveData
import com.ligq.sunnyweather.logic.dao.PlaceDao
import com.ligq.sunnyweather.logic.model.Place
import com.ligq.sunnyweather.logic.model.Weather
import com.ligq.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * 仓库类，用于获取并处理数据
 */
object Repository {
    /**
     * 搜索城市方法，使用liveData挂起函数
     */
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlace(query)
        if ("ok" == placeResponse.status) {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val dailyWeather = async { SunnyWeatherNetwork.getDailyWeather(lng, lat) }
            val realtimeWeather = async { SunnyWeatherNetwork.getRealtimeWeather(lng, lat) }

            val realtimeResponse = realtimeWeather.await()
            val dailyResponse = dailyWeather.await()

            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather =
                    Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    java.lang.RuntimeException(
                        "realtime status is ${realtimeResponse.status} " +
                                "daily status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData(context) {
            //获取数据结果
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure(e)
            }
            //发送结果给观察者，如activity，fragment
            emit(result)
        }

    /**
     * //todo 注意数据的读写不要放在主线程上，应该也是通过liveData的方式进行协程操作
     */
    fun savePlace(place: Place) {
        PlaceDao.savePlace(place)
    }

    fun getSavedPlace() = PlaceDao.getPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}