package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_rating.*


class RatingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)


        btn_clickrating.setOnClickListener {
            val ratingvalue = ratingbar.rating
            Toast.makeText(this, "Rating is:" + ratingvalue, Toast.LENGTH_SHORT) .show()

            val resultIntent = Intent()

            resultIntent.putExtra("rating_key", ratingvalue)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

}