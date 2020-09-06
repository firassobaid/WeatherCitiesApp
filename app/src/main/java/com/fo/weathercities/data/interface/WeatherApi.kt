package com.fo.weathercities.data.`interface`

import com.fo.weathercities.data.model.ApiWeather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/data/2.5/weather")
    fun getWeather(
        @Query("id") id: String,
        @Query("units") units: String,
        @Query("APPID") appId: String
    ): Observable<ApiWeather>
}

//?id=2643743&units=metric&APPID=227742b225a78802d4ed5b27e9b3c6b9