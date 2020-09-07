package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_create_day.*


class CreateDayActivity : AppCompatActivity()  {

    lateinit var dayText: EditText
    lateinit var btnSaveDay: Button
    var rating : Float? = null
     var date : String? =null
     var location :String? =null
     var image: Uri? =null

    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference

    companion object {
        const val RATING_NUMBER = "rating_number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_day)

        dayText = findViewById(R.id.dayPlainText)
        btnSaveDay = findViewById(R.id.btnSaveDay)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        val bundle = intent.extras
         date = bundle!!.getString("DATE")!!


        Toast.makeText(this, date , Toast.LENGTH_SHORT).show()

        btnSaveDay.setOnClickListener {
            if (dayPlainText.text.isEmpty() || rating==null || location==null || image==null)
                Toast.makeText(this, "Please provide us they information" , Toast.LENGTH_SHORT).show()
            else
            {
                saveDayDataToFireBaseDatabase()
              //  finish()
            }


        }

        btn_raiting.setOnClickListener {
            val intent = Intent(this, RatingActivity::class.java)
            startActivityForResult(intent,1)
        }

        btn_image.setOnClickListener {
            val intent = Intent(this, ImageUpload::class.java)
            startActivity(intent)
        }

        btn_location.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivityForResult(intent,2)
        }

        btn_image.setOnClickListener {
            val intent = Intent(this, ImageUpload::class.java)
            startActivityForResult(intent,3)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK) {

                    rating = data!!.getFloatExtra("rating_key",0.0f)
                    Toast.makeText(this, rating.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            2 -> {
                if (resultCode == Activity.RESULT_OK) {
                    location = data!!.getStringExtra("location_key")!!
                    Toast.makeText(this, location, Toast.LENGTH_SHORT).show()
                }
            }

            3 -> {
                 if (resultCode == Activity.RESULT_OK)
                     image = data!!.getParcelableExtra<Uri>("image_key")!!
                    Toast.makeText(this, image!!.toString(), Toast.LENGTH_SHORT).show()

                 }

        }
    }

    private fun saveDayDataToFireBaseDatabase (){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/days/$date")
        val dayData  = DayData(rating!!  ,location!! ,image!!.toString() , dayText.text.toString() )
        //val imageUri = Uri.parse(image)
        storageReference.child("Photos/$date.jpg")
            .putFile(image!!)
            .addOnCompleteListener{
                ref.setValue(dayData).addOnCompleteListener {
                    finish()
                }
            }
            .addOnFailureListener{
                Log.e("Upload failed:", it.message ?: "")
            }
        }
    }

data class DayData(val rating:Float, val location:String, val image: String, val dayText : String)













