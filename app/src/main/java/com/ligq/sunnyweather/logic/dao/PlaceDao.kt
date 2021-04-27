package com.ligq.sunnyweather.logic.dao

import android.content.Context
import com.google.gson.Gson
import com.ligq.sunnyweather.SunnyWeatherApplication
import com.ligq.sunnyweather.logic.model.Place

object PlaceDao {
    fun savePlace(place: Place) {
        sharedPreference().edit()
            .putString("place", Gson().toJson(place))
            .apply()
    }

    fun getPlace(): Place {
        val placeJson = sharedPreference().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharedPreference().contains("place")

    private fun sharedPreference() = SunnyWeatherApplication.context.getSharedPreferences(
        "sunny_weather",
        Context.MODE_PRIVATE
    )
}