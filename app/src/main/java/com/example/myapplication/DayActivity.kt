package com.example.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_day.*

class DayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val bundle = intent.extras
        val date = bundle!!.getString("DATE")!!

        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.reference

        FirebaseDatabase.getInstance().getReference("/users/$uid/days/$date").addValueEventListener(object :
            ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DayActivity, "error", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                    textViewCurrentDate.text = date.replace("_",".")
                    textViewCurrentLocation.text = snapshot.child("location").value as String
                    textViewCurrentRating.text = (snapshot.child("rating").value as Double).toString()
                    textViewCurrentDay.text = snapshot.child("dayText").value as String
                }
        })

        storageReference.child("Photos/$date.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
            imageViewCurrentImage.setImageBitmap(bitmap)
        }

    }
}