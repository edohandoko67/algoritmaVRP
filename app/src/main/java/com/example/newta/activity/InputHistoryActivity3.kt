package com.example.newta.activity

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.newta.R
import com.example.newta.model.StoryModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_input_history3.*
import java.util.*

class InputHistoryActivity3 : AppCompatActivity() {
    private lateinit var story: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_history3)
        supportActionBar?.hide()
        story = FirebaseDatabase.getInstance().getReference("histori")
        etTanggal.setOnClickListener{
            showDatePicker()
        }
        btnAddStori.setOnClickListener {
            savesData()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
                etTanggal.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun savesData() {
        val weekly = etWeek.text.toString().trim()
        val total = etSizeSampah.text.toString().trim()
        val tanggal = etTanggal.text.toString().trim()
        val nopol = etNopol.text.toString().trim()
        val distances = etJarak.text.toString().trim()
        val waktu = etWaktu.text.toString().trim()
        val bahanBakar = etBahanBakar.text.toString().trim()
        val priceBahan = etPrice.text.toString().trim()
        if (weekly.isEmpty() || total.isEmpty() || nopol.isEmpty() || distances.isEmpty() || waktu.isEmpty() || bahanBakar.isEmpty() || priceBahan.isEmpty() || tanggal.isEmpty()) {
            Toast.makeText(this, "Isi semua field!", Toast.LENGTH_SHORT).show()
        }else {
            val totalSize = total.toInt()
            val distanceIn = distances.toInt()
            val waktuIn = waktu.toInt()
            val bahanBakarIn = bahanBakar.toInt()
            val priceIn = priceBahan.toInt()

            val wasteId = story.push().key
            //val currentDate = getCurrentDate()
            val STDS = StoryModel(wasteId.toString(), weekly, totalSize, tanggal, nopol, distanceIn, waktuIn, bahanBakarIn, priceIn)
            story.child(wasteId.toString()).setValue(STDS).addOnSuccessListener {
                Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    @SuppressLint("SimpleDateFormat")
//    private fun getCurrentDate(): String {
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        val date = Date()
//        return dateFormat.format(date)
//    }
}