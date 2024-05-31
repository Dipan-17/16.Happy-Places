package com.example.happyplaces.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.happyplaces.databases.DatabaseHandler
import com.example.happyplaces.databinding.ActivityMainBinding
import com.example.happyplaces.models.HappyPlaceModel

class MainActivity : AppCompatActivity() {

    private var binding:ActivityMainBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.fabAddHappyPlace?.setOnClickListener {
             val intent=Intent(this@MainActivity, AddHappyActivity::class.java)
             startActivity(intent)
                //Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show()
        }
        getHappyPlacesListFromLocalDB()
    }

    private fun getHappyPlacesListFromLocalDB(){
        val dbHandler=DatabaseHandler(this)
        val getHappyPlaceList:ArrayList<HappyPlaceModel> = dbHandler.getHappyPlacesList()
        //put all of them in a recycler view
        if(getHappyPlaceList.size>0){
            for(i in getHappyPlaceList){
                Log.e("Title",i.title)
                Log.e("Image",i.image)
            }
        }
    }

}