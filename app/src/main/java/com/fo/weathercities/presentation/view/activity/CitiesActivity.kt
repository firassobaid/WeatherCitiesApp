package com.fo.weathercities.presentation.view.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.fo.weathercities.R
import com.fo.weathercities.databinding.ActivityCitiesBinding
import com.fo.weathercities.presentation.view.fragment.CitiesFragment

class CitiesActivity : FragmentActivity() {

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

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}