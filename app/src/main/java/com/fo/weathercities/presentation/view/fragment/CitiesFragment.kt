package com.fo.weathercities.presentation.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fo.weathercities.R
import com.fo.weathercities.data.model.City
import com.fo.weathercities.databinding.CitiesFragmentBinding
import com.fo.weathercities.presentation.presenter.CitiesPresenter
import com.fo.weathercities.presentation.view.`interface`.CitiesView
import com.fo.weathercities.presentation.view.intent.CitiesInit
import com.fo.weathercities.presentation.view.intent.CityClickIntent
import com.fo.weathercities.presentation.view.item.CityItem
import com.fo.weathercities.presentation.view.viewstate.CitiesViewState
import com.hannesdorfmann.mosby3.mvi.MviFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.cities_fragment.*

const val TAG = "CitiesFragment"

class CitiesFragment : MviFragment<CitiesView, CitiesPresenter>(), CitiesView {

    private lateinit var binding: CitiesFragmentBinding
    private lateinit var citiesRecycler: RecyclerView
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>
    private lateinit var toDetailsCallback: ToDetailsInterface
    private var presenter: CitiesPresenter = CitiesPresenter()
    private val cityClick = PublishSubject.create<City>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CitiesFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
    }

    override fun onPause() {
        cityClick.onNext(City.FAKE_CITY)
        super.onPause()
    }

    override fun createPresenter(): CitiesPresenter = presenter

    private object HOLDER {
        val INSTANCE = CitiesFragment()
    }

    companion object {
        val instance: CitiesFragment by lazy { HOLDER.INSTANCE }
    }

    private fun setupRecycler() {
        citiesRecycler = citiesList
        val linearLayoutManager = LinearLayoutManager(context)
        citiesRecycler.layoutManager = linearLayoutManager
        adapter = GroupAdapter()
        citiesRecycler.adapter = adapter
        //Divider item decoration
        citiesRecycler.addItemDecoration(
            DividerItemDecoration(
                context,
                linearLayoutManager.orientation
            )
        )
    }

    override fun citiesInit(): Observable<CitiesInit> = Observable.just(
        CitiesInit(
            context?.resources?.openRawResource(
                R.raw.cities
            )
        )
    )

    override fun cityClick(): Observable<CityClickIntent> =
        cityClick.hide().map { CityClickIntent(it) }

    override fun render(viewState: CitiesViewState) {
        Log.i(TAG, "View state - $viewState")
        setData(viewState.cities.toGroupieItems(cityClick))
        isLoading(viewState.loading)
        viewState.cityClicked?.let {
            toDetailsCallback.onDetailsPressed(city = viewState.cityClicked)
        }
    }

    private fun setData(cities: List<Item>) {
        if (cities.isEmpty()) {
            empty_view.visible()
            citiesRecycler.gone()
        } else {
            empty_view.gone()
            citiesRecycler.visible()
            adapter.clear()
            adapter.addAll(cities)
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            loading_indicator.visible()
            empty_view.gone()
        } else {
            loading_indicator.gone()
        }
    }

    fun setToDetailsListener(callback: ToDetailsInterface) {
        this.toDetailsCallback = callback
    }
}

interface ToDetailsInterface {
    fun onDetailsPressed(city: City)
}

private fun List<City>.toGroupieItems(cityClickSubject: PublishSubject<City>): List<Item> =
    this.map { CityItem(it, cityClickSubject) }

fun View.visible() {
    this.visibility = View.VISIBLE
}

private fun View.gone() {
    this.visibility = View.GONE
}