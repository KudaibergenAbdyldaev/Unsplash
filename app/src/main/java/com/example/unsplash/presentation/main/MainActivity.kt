package com.example.unsplash.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.unsplash.R
import com.example.unsplash.databinding.ActivityMainBinding
import com.example.unsplash.presentation.photos.PopularFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, PopularFragment())
                .commit()
        }

        setContentView(binding.root)
    }

}