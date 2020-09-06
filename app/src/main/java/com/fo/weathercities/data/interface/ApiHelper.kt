package com.fo.weathercities.data.`interface`

import com.fo.weathercities.data.model.ApiWeather
import io.reactivex.Observable

interface ApiHelper {
    fun getWeather(cityId: String): Observable<ApiWeather>
}