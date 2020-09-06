package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    lateinit var  sharedpreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)


        sharedpreferences = applicationContext.getSharedPreferences("Preferences", 0)
        val UserLoggedIn = sharedpreferences.getBoolean("KEY_LOGIN", false)


        if (UserLoggedIn) {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        login_button_login.setOnClickListener {
            performLogin()
        }

        create_new_account_textview.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out email/password.", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                sharedpreferences.edit().putBoolean("KEY_LOGIN",true).apply()
                val user = FirebaseAuth.getInstance().currentUser
                updateUI(user)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to log in.", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
    }

    private fun updateUI(currentUser: FirebaseUser?){
        if (currentUser != null) {

                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show()
            }

        }


    }

