package com.example.newta.activity

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.newta.R
import com.example.newta.model.LatLngModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var etLat: EditText
    private lateinit var etLong: EditText
    private lateinit var spinnerDatabase: Spinner
    private lateinit var btnAdd: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        database = FirebaseDatabase.getInstance().reference
        val databaseNames = listOf("db", "db2", "db3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, databaseNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDatabase = findViewById(R.id.spinnerDatabase)
        spinnerDatabase.adapter = adapter
        etLat = findViewById(R.id.etLat)
        etLong = findViewById(R.id.etLong)
        btnAdd = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val lat = etLat.text.toString().trim()
            val long = etLong.text.toString().trim()
            val name = etAddress.text.toString().trim()
            val size = etSize.text.toString().trim()
            val selectedDatabase = spinnerDatabase.selectedItem.toString()
            saveData(selectedDatabase, lat,long,name,size)
        }
    }

    private fun saveData(
        databaseName: String,
        lat: String,
        long: String,
        name: String,
        size: String) {
        if (lat.isEmpty() || long.isEmpty() || name.isEmpty() || size.isEmpty()){
            Toast.makeText(this, "isi field!", Toast.LENGTH_SHORT).show()
        }else
        {
            val lats = lat.toDoubleOrNull()
            val longs = long.toDoubleOrNull()
            val sizess = size.toInt()

            if (lats == null || longs == null) {
                Toast.makeText(this, "Lat dan long harus berupa angka!", Toast.LENGTH_SHORT).show()
            }
            else {
                val database = FirebaseDatabase.getInstance().getReference(databaseName)
                val routeId = database.push().key
                val STD = LatLngModel(routeId.toString(),lat.toDouble(),long.toDouble(), name, sizess )
                database.child(routeId.toString()).setValue(STD)
                database.child(routeId.toString()).setValue(STD).addOnCompleteListener {
                    Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}