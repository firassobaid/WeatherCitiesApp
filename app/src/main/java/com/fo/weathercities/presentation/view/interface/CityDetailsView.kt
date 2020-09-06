package com.fo.weathercities.presentation.view.`interface`

import com.fo.weathercities.presentation.view.intent.ConvertInit
import com.fo.weathercities.presentation.view.intent.DetailsInit
import com.fo.weathercities.presentation.view.viewstate.CityDetailsViewState
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface CityDetailsView : MvpView {
    fun init(): Observable<DetailsInit>
    fun convert(): Observable<ConvertInit>
    fun render(viewState: CityDetailsViewState)
}