package com.example.happyplaces

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.happyplaces.databinding.ActivityAddHappyBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
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
        binding?.tvAddImage?.setOnClickListener(this@AddHappyActivity)
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

            binding?.tvAddImage?.id -> {
                val pictureDialog= AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems= arrayOf("Select photo from gallery","Capture photo from camera")
                pictureDialog.setItems(pictureDialogItems){
                    _, which ->
                    when(which){
                        0 -> {
                            callChoosePhotoFromGallery()
                        }
                        1 -> {
                            callChoosePhotoFromCamera()
                        }
                    }
                }
                pictureDialog.show()
            }
        }
    }

    private fun callChoosePhotoFromCamera() {
        TODO("Not yet implemented")
    }

    private fun callChoosePhotoFromGallery() {
        Dexter.withActivity(this)
            .withPermissions(
                android.Manifest.permission.READ_MEDIA_IMAGES
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if(report!!.areAllPermissionsGranted()){
                        Toast.makeText(this@AddHappyActivity,"Read permission granted",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread().check()
    }

    private fun showRationalDialogForPermissions(){
        AlertDialog.Builder(this).setMessage("It looks like you have turned off permissions required for this feature. It can be enabled under the Application settings")
            .setPositiveButton("GO TO SETTINGS"){
                _,_ ->
                try{
                    val intent= Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri= Uri.fromParts("package",packageName,null)
                    intent.data=uri
                    startActivity(intent)
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }
            .setNegativeButton("CANCEL"){
                dialog,_ ->
                dialog.dismiss()
            }.show()
    }
    private fun updateDateInView(){
        val format="dd.MM.yyyy"
        val sdf=SimpleDateFormat(format, Locale.getDefault())
        binding?.etDate?.setText(sdf.format(cal.time).toString())
    }




}