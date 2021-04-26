package com.ligq.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ligq.sunnyweather.logic.Repository
import com.ligq.sunnyweather.logic.model.Place

class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    val placeList = ArrayList<Place>()

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }
}