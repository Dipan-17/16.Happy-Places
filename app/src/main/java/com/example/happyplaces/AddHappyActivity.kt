package com.example.happyplaces

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplaces.databinding.ActivityAddHappyBinding
import com.example.happyplaces.databinding.ActivityMainBinding

class AddHappyActivity : AppCompatActivity() {
    private var binding: ActivityAddHappyBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityAddHappyBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //display back button on toolbar
        setSupportActionBar(binding?.toolbarAddPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolbarAddPlace?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}