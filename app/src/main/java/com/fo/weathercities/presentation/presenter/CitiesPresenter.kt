package com.fo.weathercities.presentation.presenter

import android.util.Log
import com.fo.weathercities.data.model.CitiesList
import com.fo.weathercities.data.model.City
import com.fo.weathercities.presentation.view.`interface`.CitiesView
import com.fo.weathercities.presentation.view.mutation.CitiesInitMutation
import com.fo.weathercities.presentation.view.mutation.CitiesMutation
import com.fo.weathercities.presentation.view.mutation.CityClickMutation
import com.fo.weathercities.presentation.view.viewstate.CitiesViewState
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.InputStream
import java.util.concurrent.TimeUnit

const val TAG = "CitiesPresenter"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

class CitiesPresenter : MviBasePresenter<CitiesView, CitiesViewState>() {
    override fun bindIntents() {
        val init: Observable<CitiesInitMutation> = intent { it.citiesInit() }
            .delay(3, TimeUnit.SECONDS)
            .map { CitiesInitMutation(dataFromSource(it.stream)) }
            .onErrorReturn { CitiesInitMutation(emptyList()) }
            .doOnError { e -> Log.e(TAG, "Error getting cities list", e) }


        val cityClick: Observable<CityClickMutation> = intent { it.cityClick() }
            .map { CityClickMutation(it.city) }

        val state =
            Observable.merge(init, cityClick)
                .scan<CitiesViewState>(CitiesViewState(loading = true), this::reduce)
                .observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())

        subscribeViewState(state, CitiesView::render)
    }

    private fun reduce(oldState: CitiesViewState, mutation: CitiesMutation): CitiesViewState {
        return when (mutation) {
            is CitiesInitMutation -> oldState.copy(loading = false, cities = mutation.cities)
            is CityClickMutation -> oldState.copy(cityClicked = mutation.city)
            else -> oldState
        }
    }

    private fun dataFromSource(stream: InputStream?): List<City> {
        val inputString = stream?.bufferedReader().use { it?.readText() }
        val jsonAdapter: JsonAdapter<CitiesList> = moshi.adapter(CitiesList::class.java)
        val citiesList =
            inputString?.let { jsonAdapter.fromJson(inputString) } ?: CitiesList(emptyList())
        return citiesList.cities
    }
}
