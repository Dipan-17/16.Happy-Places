package com.example.happyplaces.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context.*
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.happyplaces.databases.DatabaseHandler
import com.example.happyplaces.databinding.ActivityAddHappyBinding
import com.example.happyplaces.models.HappyPlaceModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Calendar
import java.util.Locale
import java.util.UUID


//here we are passing the View.OnClickListener and implementing the onClick method
//this allows us to define all the onClick events together in one place
//this time the entire class is an onclickListener
class AddHappyActivity : AppCompatActivity(),View.OnClickListener {
    private var binding: ActivityAddHappyBinding?= null

    //to get the date from calendar and write in edit text
    private var cal=Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    //passing varibale
    private var saveImageToInternalStorage:Uri?=null
    private var mLatitute:Double=0.0
    private var mLongitude:Double=0.0

    //companion object used to create static variables and constant
    companion object{
        //to open gallery and camera
        private const val GALLERY=1
        private const val CAMERA=2

        //to store image in local
        private const val IMAGE_DIRECTORY="HappyPlacesImages"
    }

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

        //set the date in the edit text to current system date
        updateDateInView()

        binding?.etDate?.setOnClickListener(this@AddHappyActivity)
        binding?.tvAddImage?.setOnClickListener(this@AddHappyActivity)
        binding?.btnSave?.setOnClickListener ( this@AddHappyActivity )
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            binding?.etDate?.id -> {
                DatePickerDialog(
                    this@AddHappyActivity,
                    dateSetListener,cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()

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

            binding?.btnSave?.id -> {
                //Toast.makeText(this@AddHappyActivity,"Save button clicked",Toast.LENGTH_SHORT).show()
                when{
                    binding?.etTitle?.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this@AddHappyActivity,
                            "Please enter title",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding?.etDescription?.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this@AddHappyActivity,
                            "Please enter description",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                    binding?.etLocation?.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this@AddHappyActivity,
                            "Please enter location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    saveImageToInternalStorage==null->{
                        Toast.makeText(
                            this@AddHappyActivity,
                            "Please select an image",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        val happyPlaceModel= HappyPlaceModel(
                            0,//auto incremented
                            binding?.etTitle?.text.toString(),
                            saveImageToInternalStorage.toString(),
                            binding?.etDescription?.text.toString(),
                            binding?.etDate?.text.toString(),
                            binding?.etLocation?.text.toString(),
                            mLatitute,
                            mLongitude
                        )

                        val dbHandler= DatabaseHandler(this)
                        val addHappyPlaceResult=dbHandler.addHappyPlace(happyPlaceModel)
                        if(addHappyPlaceResult>0){
                            Toast.makeText(this,"The happy place details are added successfully",Toast.LENGTH_SHORT).show()

                            //cause main activity called with result
                            setResult(Activity.RESULT_OK)

                            finish() //close the add activity
                        }else{
                            Toast.makeText(this,"Error while adding the happy place details",Toast.LENGTH_SHORT).show()
                            finish()
                        }

                    }
                }
            }
        }
    }


    private fun callChoosePhotoFromCamera() {
        Dexter.withActivity(this)
            .withPermissions(
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if(report!!.areAllPermissionsGranted()){
                        //pick image from camera
                        val cameraIntent=Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE
                        )
                        startActivityForResult(cameraIntent, CAMERA)
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

    //this method uses dexter to handle permissions
    private fun callChoosePhotoFromGallery() {
        Dexter.withActivity(this)
            .withPermissions(
                android.Manifest.permission.READ_MEDIA_IMAGES,
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if(report!!.areAllPermissionsGranted()){
                        //Toast.makeText(this@AddHappyActivity,"Read permission granted",Toast.LENGTH_SHORT).show()
                        //pick image from gallery
                        val galleryIntent=Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                        startActivityForResult(galleryIntent, GALLERY)
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


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //if the result is ok
        if (resultCode == Activity.RESULT_OK) {

            //activity result is of gallery
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                        binding?.ivPlaceImage?.setImageBitmap(selectedImageBitmap)

                        //store in internal and pass the uri to global variable
                        saveImageToInternalStorage=saveImageToInternalStorage(selectedImageBitmap)
                        Log.e("Saved Image Path", "Path :: $saveImageToInternalStorage")

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddHappyActivity,
                            "Failed to load image from gallery",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }else{
                    Toast.makeText(this@AddHappyActivity,"Invalid URI",Toast.LENGTH_SHORT).show()
                }
            }

            //activity result from camera
            else if (requestCode == CAMERA) {
                val thumbnail:Bitmap = data?.extras?.get("data") as Bitmap
                binding?.ivPlaceImage?.setImageBitmap(thumbnail)

                //store in internal
                saveImageToInternalStorage=saveImageToInternalStorage(thumbnail)
                Log.e("Saved Image Path", "Path :: $saveImageToInternalStorage")

            }
        }
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


    private fun saveImageToInternalStorage(bitmap: Bitmap):Uri{
        val wrapper= ContextWrapper(applicationContext)

        //creating a file
        var file=wrapper.getDir(IMAGE_DIRECTORY, MODE_PRIVATE) //mode private only makes it accessible from the calling application
        file= File(file,"${UUID.randomUUID()}.jpg")

        try{
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
            Toast.makeText(this@AddHappyActivity,"Image saved successfully",Toast.LENGTH_SHORT).show()
        }catch (e:IOException){
            e.printStackTrace()
            Toast.makeText(this@AddHappyActivity,"Failed to save image",Toast.LENGTH_SHORT).show()
        }

        return Uri.parse(file.absolutePath)
    }

}