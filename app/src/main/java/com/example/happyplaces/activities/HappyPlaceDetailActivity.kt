package com.example.happyplaces.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplaces.R
import com.example.happyplaces.databinding.ActivityHappyPlaceDetailBinding
import com.example.happyplaces.models.HappyPlaceModel

class HappyPlaceDetailActivity : AppCompatActivity() {

    private var binding:ActivityHappyPlaceDetailBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityHappyPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        var happyPlaceDetailModel: HappyPlaceModel?=null
        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            happyPlaceDetailModel=intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as HappyPlaceModel //we typecast it to HappyPlaceModel
        }
        if(happyPlaceDetailModel!=null){
            setSupportActionBar(binding?.toolbarHappyPlaceDetail)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            binding?.toolbarHappyPlaceDetail?.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            supportActionBar?.title=happyPlaceDetailModel.title

            binding?.ivPlaceImage?.setImageURI(Uri.parse(happyPlaceDetailModel.image))
            binding?.tvDescription?.text=happyPlaceDetailModel.description
            binding?.tvLocation?.text=happyPlaceDetailModel.location
        }
    }
}