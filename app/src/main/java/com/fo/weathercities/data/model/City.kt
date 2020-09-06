package com.fo.weathercities.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val city: String,
    val id: String,
    val weatherDescription: String? = null,
    val weatherIcon: String? = null,
    val temperature: String? = null,
    val lowTemperature: String? = null,
    val highTemperature: String? = null
) : Parcelable