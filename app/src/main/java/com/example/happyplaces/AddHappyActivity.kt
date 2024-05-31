package com.example.happyplaces

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.happyplaces.databinding.ActivityAddHappyBinding
import com.example.happyplaces.databinding.ActivityMainBinding
import java.util.Calendar
import java.util.Locale

//here we are passing the View.OnClickListener and implementing the onClick method
//this allows us to define all the onClick events together in one place
//this time the entire class is an onclickListener
class AddHappyActivity : AppCompatActivity(),View.OnClickListener {
    private var binding: ActivityAddHappyBinding?= null

    private var cal=Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

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

        dateSetListener=DatePickerDialog.OnDateSetListener{
            view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

        binding?.etDate?.setOnClickListener(this@AddHappyActivity)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            binding?.etDate?.id -> {
                DatePickerDialog(this@AddHappyActivity,
                    dateSetListener,cal.get(Calendar.YEAR)
                    ,cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()

                    //we have declared the calendar as global variable so that we can access it in the dateSetListener
                    updateDateInView()
            }
        }
    }

    private fun updateDateInView(){
        val format="dd.MM.yyyy"
        val sdf=SimpleDateFormat(format, Locale.getDefault())
        binding?.etDate?.setText(sdf.format(cal.time).toString())
    }




}