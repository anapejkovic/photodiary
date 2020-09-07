package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_list.*


class ListActivity : AppCompatActivity(), OnDayClickListener {
    lateinit var  sharedpreferences: SharedPreferences

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        sharedpreferences = applicationContext.getSharedPreferences("Preferences", 0)

        btnAddDay.setOnClickListener {
            val intent = Intent(this, DataPickerActivity::class.java)
            startActivity(intent)
        }

        logout_button_logout.setOnClickListener {
            sharedpreferences.edit().putBoolean("KEY_LOGIN",false).apply()
            finish()
        }

        //da zna točno od kojeg korisnika uzima dane tražimo uid
        val uid = FirebaseAuth.getInstance().uid ?: ""
         FirebaseDatabase.getInstance().getReference("/users/$uid/days").addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListActivity, "error", Toast.LENGTH_SHORT).show()
            }
            //sve datume stavaljam u listu iz baze
            override fun onDataChange(snapshot: DataSnapshot) {
                val myDataset = mutableListOf<String>()
                for (dateSnapshot in snapshot.children) {
                    val date = dateSnapshot.key!!.replace("_", ".")
                    myDataset.add(date)
                }

                viewAdapter = MyAdapter(myDataset, this@ListActivity)
                viewManager = LinearLayoutManager(this@ListActivity)
                recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
            }
        })

    }

    class MyAdapter(private val myDataset: List<String>, private val listener: OnDayClickListener) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): MyAdapter.MyViewHolder {

            val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_text_view, parent, false) as android.widget.TextView

            return MyViewHolder(textView)
        }

        // mora znati pozicije
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            holder.textView.text = myDataset[position]
                //zbog pozicije ovdje implemenitano
                holder.textView.setOnClickListener {
                listener.OnDayClick(myDataset[position]) //iz te liste trebam string s tocno te pozicije
            }
        }

        override fun getItemCount() = myDataset.size
    }

    override fun OnDayClick(date : String) {
            //ovaj aktiviti je morao implementirati OnDayClick listener jer on zna kako prebaciti iz jednoga u drugi
            val intent = Intent(this, DayActivity::class.java)
            startActivity(intent)
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show()
    }

}

interface OnDayClickListener{
    fun OnDayClick( date : String)
}