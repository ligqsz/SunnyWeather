package com.ligq.sunnyweather.logic.model

data class Weather(val realtime: RealTimeResponse.Realtime, val daily: DailyResponse.Daily)