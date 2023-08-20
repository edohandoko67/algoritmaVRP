package com.example.newta.UI.officer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.newta.R
import com.example.newta.model.LatLngModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_rute_off.*

class AddRuteOffActivity : AppCompatActivity() {
    private lateinit var databaseRefs: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rute_off)
        supportActionBar?.hide()
        databaseRefs = FirebaseDatabase.getInstance().getReference("db")

        btnAddOff.setOnClickListener {
            savesData()
        }
    }

    private fun savesData() {
        val latitudes = etLatOff.text.toString()
        val longitudes = etLongOff.text.toString()
        val address = etAddressOff.text.toString()
        val sizes = etSizeOff.text.toString()

        if (latitudes.isEmpty() || longitudes.isEmpty() || address.isEmpty() || sizes.isEmpty()){
            Toast.makeText(this, "Isi semua field", Toast.LENGTH_SHORT).show()
        }
        else {
            val latitude = latitudes.toDoubleOrNull()
            val longitude = longitudes.toDoubleOrNull()
            val size = sizes.toInt()

            if (latitude == null || longitude == null) {
                Toast.makeText(this, "Lat dan long harus berupa angka!", Toast.LENGTH_SHORT).show()
            }
            else {
                val IdRoute = databaseRefs.push().key
                val STO = LatLngModel(IdRoute.toString(), latitudes.toDouble(), longitudes.toDouble(), address, size )
                databaseRefs.child(IdRoute.toString()).setValue(STO)
                databaseRefs.child(IdRoute.toString()).setValue(STO).addOnCompleteListener {
                    Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}