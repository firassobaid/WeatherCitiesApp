package com.fo.weathercities.data.model

import com.squareup.moshi.Json

data class MainWeather(
    @field:Json(name = "temp") val temp: String?,
    @field:Json(name = "temp_min") val temp_min: String?,
    @field:Json(name = "temp_max") val temp_max: String?
)