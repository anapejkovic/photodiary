package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_data_picker.*
import java.util.*

class DataPickerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_picker)

        //Calendar
        val c = Calendar.getInstance()
        val year = c.get (Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get (Calendar.DAY_OF_MONTH)

        //button click to show DatePickerDialog
        pickDateBtn.setOnClickListener{
            val dpd =DatePickerDialog(this, DatePickerDialog.OnDateSetListener {view , mYear, mMonth, mDay ->

                //set to text view
                dataTextview.setText(""+ mDay +"/"+ (mMonth+1) +"/"+ mYear)
            }, year, month, day)

            //ograničenje datuma
           dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            /* show dialog */
            dpd.show()
        }
        CreateDayBtn.setOnClickListener {

            val intent = Intent (this, CreateDayActivity :: class.java)


            val bundle = Bundle()

            bundle.putString("DATE", dataTextview.text.toString().replace("/","_"))
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }
}