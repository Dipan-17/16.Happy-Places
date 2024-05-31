package com.example.happyplaces.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyplaces.databases.DatabaseHandler
import com.example.happyplaces.databinding.ActivityMainBinding
import com.example.happyplaces.models.HappyPlaceModel
import com.happyplaces.adapters.HappyPlacesAdapter

class MainActivity : AppCompatActivity() {

    private var binding:ActivityMainBinding?= null

    companion object{
        const val ADD_PLACE_ACTIVITY_REQUEST_CODE=1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarMain)

        binding?.fabAddHappyPlace?.setOnClickListener {
             val intent=Intent(this@MainActivity, AddHappyActivity::class.java)
             //startActivity(intent)
             //the database is only refreshed on create so we need to refresh it
            //so we will start the add activity for result
            startActivityForResult(intent,ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }
        getHappyPlacesListFromLocalDB()
    }

    private fun getHappyPlacesListFromLocalDB(){
        val dbHandler=DatabaseHandler(this)
        val getHappyPlaceList:ArrayList<HappyPlaceModel> = dbHandler.getHappyPlacesList()
        //put all of them in a recycler view
        if(getHappyPlaceList.size>0){
            binding?.rvHappyPlaces?.visibility=android.view.View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility=android.view.View.GONE
            setupHappyPlacesRecyclerView(getHappyPlaceList)
        }else{
            binding?.rvHappyPlaces?.visibility=android.view.View.GONE
            binding?.tvNoRecordsAvailable?.visibility=android.view.View.VISIBLE
        }
    }

    private fun setupHappyPlacesRecyclerView(happyPlaceList:ArrayList<HappyPlaceModel>){
        val adapter=HappyPlacesAdapter(happyPlaceList)
        binding?.rvHappyPlaces?.setHasFixedSize(true)
        binding?.rvHappyPlaces?.layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding?.rvHappyPlaces?.adapter=adapter


        adapter.setOnClickListener(object: HappyPlacesAdapter.onClickInterface{
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent=Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                startActivity(intent)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode== ADD_PLACE_ACTIVITY_REQUEST_CODE){
            if(resultCode== RESULT_OK){
                //call this function to refresh the list
                getHappyPlacesListFromLocalDB()
            }else{
                Log.e("Activity","Cancelled or Back pressed")
            }
        }
    }

}