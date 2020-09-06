package com.fo.weathercities.data.service

import com.fo.weathercities.data.`interface`.ApiHelper
import com.fo.weathercities.data.`interface`.WeatherApi
import com.fo.weathercities.data.model.ApiWeather
import io.reactivex.Observable

class ApiHelperImpl(private val apiService: WeatherApi) : ApiHelper {
    override fun getWeather(cityId: String): Observable<ApiWeather> =
        apiService.getWeather(cityId, "metric", "227742b225a78802d4ed5b27e9b3c6b9")
}
