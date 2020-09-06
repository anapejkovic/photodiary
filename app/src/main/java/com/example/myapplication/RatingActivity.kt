package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_rating.*


class RatingActivity : AppCompatActivity() {

    lateinit var btn : Button
    lateinit var rb : RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        btn = findViewById<View>(R.id.btn_clickrating) as Button
        rb = findViewById<View>(R.id.ratingbar) as RatingBar

        btn.setOnClickListener {
            val ratingvalue = ratingbar.rating
            Toast.makeText(this, "Rating is:" + ratingvalue, Toast.LENGTH_SHORT) .show()

            val resultIntent = Intent()

            resultIntent.putExtra("rating_key", ratingvalue)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

}