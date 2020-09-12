package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_data_picker.*
import java.util.*

class DatePickerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_picker)

        //Calendar
        val calendar = Calendar.getInstance()
        val year = calendar.get (Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get (Calendar.DAY_OF_MONTH)

        //button click to show DatePickerDialog
        pickDateBtn.setOnClickListener{
            //kupi datum koji mi izaberemo
            val dpd =DatePickerDialog(this, DatePickerDialog.OnDateSetListener {view , mYear, mMonth, mDay ->

                //ispisuje pokupljeni datum
                dataTextview.setText(""+ mDay +"/"+ (mMonth+1) +"/"+ mYear)
            }, year, month, day)

            //ograničenje datuma
           dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            /* show dialog */
            dpd.show()
        }

        CreateDayBtn.setOnClickListener {

            val intent = Intent (this, CreateDayActivity :: class.java)

            //saljem paket s datumom
            val bundle = Bundle()

            bundle.putString("DATE", dataTextview.text.toString().replace("/","_"))
            intent.putExtras(bundle)
            startActivity(intent)
            finish()

        }
    }
}