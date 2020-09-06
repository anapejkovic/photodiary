package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_day.*


class CreateDayActivity : AppCompatActivity()  {

    lateinit var dayText: EditText
    lateinit var btnSaveDay: Button
    var rating : Float = 0.0f
    lateinit var date : String
    lateinit var location :String
    lateinit var image: String

    companion object {
        const val RATING_NUMBER = "rating_number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_day)

        dayText = findViewById(R.id.DayPlainText)
        btnSaveDay = findViewById(R.id.btnSaveDay)

        val bundle = intent.extras
         date = bundle!!.getString("DATE")!!


        Toast.makeText(this, date , Toast.LENGTH_SHORT).show()

        btnSaveDay.setOnClickListener {
           // saveDiary()
            if (textViewday==null)
                Toast.makeText(this, "Please tell us how was your day" , Toast.LENGTH_SHORT).show()
            else
                saveDayDataToFireBaseDatabase()
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

         fun saveDiary() {
            val textofDay = DayPlainText.text.toString().trim()

            if (textofDay.isEmpty()) {
                Toast.makeText(this, "Please tell me how was your day", Toast.LENGTH_SHORT).show()
                return
            }

            val ref = FirebaseDatabase.getInstance().getReference("Days")
            val dayId = ref.push().key
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
        }
    }

    private fun saveDayDataToFireBaseDatabase (){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/days/$date")

        val dayData  = DayData(rating  ,location , "" , dayText.text.toString() )
        ref.setValue(dayData)

    }
}
class DayData(val rating:Float, val location:String, val image: String, val DayText : String)













