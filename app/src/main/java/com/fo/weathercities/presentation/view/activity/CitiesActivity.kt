package com.fo.weathercities.presentation.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.fo.weathercities.R
import com.fo.weathercities.data.model.City
import com.fo.weathercities.databinding.ActivityCitiesBinding
import com.fo.weathercities.presentation.view.`interface`.BackPressInterface
import com.fo.weathercities.presentation.view.fragment.CitiesFragment
import com.fo.weathercities.presentation.view.fragment.CityDetailsFragment
import com.fo.weathercities.presentation.view.fragment.ToDetailsInterface

class CitiesActivity : FragmentActivity(), ToDetailsInterface {

    private lateinit var binding: ActivityCitiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CitiesFragment.instance)
                .commit()

        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is CitiesFragment) {
            fragment.setToDetailsListener(this)
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        (fragment as? BackPressInterface)?.onBackPressed()?.not()?.let {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onDetailsPressed(city: City) {
        supportFragmentManager.apply {
            beginTransaction()
                .add(R.id.fragment_container, CityDetailsFragment.newInstance(city))
                .addToBackStack(com.fo.weathercities.presentation.view.fragment.TAG)
                .commit()
        }
    }
}