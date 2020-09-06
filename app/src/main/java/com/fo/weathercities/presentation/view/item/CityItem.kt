package com.fo.weathercities.presentation.view.item

import com.fo.weathercities.R
import com.fo.weathercities.data.model.City
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.city_item.view.*

class CityItem(private val city: City, private val clickSubject: PublishSubject<City>) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.cityName.text = city.city
        viewHolder.itemView.setOnClickListener { clickSubject.onNext(city) }
    }

    override fun getLayout() = R.layout.city_item
}