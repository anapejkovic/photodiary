package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_register)

        /* auth = FirebaseAuth.getInstance() */

        register_button_register.setOnClickListener() {
            performRegister()
        }

         already_have_account_text_view.setOnClickListener {
             Log.d( "MainActivity", "Try to show login activity")
             // launch the login activity somehow

             finish()
         }
    }

    private fun performRegister(){

        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d( "Main", "Attempting to create user with email: $email")
        // da se ne crasha ako sam ostavila prazno pollje

        /*Log.d(  "MainActivity",  "Email is: " + email)
        Log.d(  "MainActivity", "password: $password")*/

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if  (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d("Main" , "Successfully created user with uid: ${it.result?.user?.uid}")
                Toast.makeText(this, "Successfully create user.", Toast.LENGTH_SHORT).show()
                saveUserToFireBaseDatabase()
            }

            .addOnFailureListener {
                Log.d("Main", "Faild to create user: ${it.message}")
                Toast.makeText(this, "Faild to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserToFireBaseDatabase (){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user  = User(uid, username_edittext_register.text.toString() )
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Main", "Finally we saved the user to Firebase Database")
            }
    }
}
class User(val uid:String, val username:String)

