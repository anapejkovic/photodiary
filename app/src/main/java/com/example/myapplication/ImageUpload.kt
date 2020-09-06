@file:Suppress("DEPRECATION")

package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_image_upload.*
import java.io.IOException

class ImageUpload : AppCompatActivity() {

    //var uid = FirebaseAuth.getInstance().currentUser!!.uid

    private val PERMISSION_CODE = 2020

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_SELECT = 10

    private var imageUri: Uri? = null
    //referenca koja pokazuje na specifičan put, gdje želimo spremiti,
    //val storageReference = Firebase.storage.reference

    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference

    lateinit var progressDialog: Dialog

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_upload)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        progressDialog = ProgressDialog(this@ImageUpload)
        progressDialog.setTitle("Uploading...")

        btnCamera.setOnClickListener(){
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                    //permission was not enabled
                    val permission = arrayOf (Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }

                else {
                    //permission alerady granted
                    openCamera()
                }
            }
            else {
                openCamera()
            }
        }

        btnChoose.setOnClickListener() {
            chooseImage()
        }

    }

    private fun chooseImage() {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type ="image/*"
            startActivityForResult(it,
                REQUEST_IMAGE_SELECT
            )
        }
    }


    private fun openCamera() {

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From camera")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                }
                else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_SELECT) {
            if (data != null && data.data != null) { //data je referenca na podatke
                imageUri = data.data //url slike
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    imageView.setImageBitmap(bitmap)
                }

                catch (e: IOException){
                    e.printStackTrace()
                }
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imageView.setImageBitmap(bitmap)
            }

            catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }





    }








