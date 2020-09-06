package com.fo.weathercities.presentation.view.`interface`

import com.fo.weathercities.presentation.view.intent.CitiesInit
import com.fo.weathercities.presentation.view.intent.CityClickIntent
import com.fo.weathercities.presentation.view.viewstate.CitiesViewState
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface CitiesView : MvpView {
    fun citiesInit(): Observable<CitiesInit>
    fun cityClick(): Observable<CityClickIntent>
    fun render(viewState: CitiesViewState)
}