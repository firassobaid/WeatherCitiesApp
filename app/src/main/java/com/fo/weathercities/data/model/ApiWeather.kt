package com.fo.weathercities.data.model

import com.squareup.moshi.Json

data class ApiWeather(
    @field:Json(name = "weather") val weather: List<Weather>?,
    @field:Json(name = "main") val main: MainWeather?
)