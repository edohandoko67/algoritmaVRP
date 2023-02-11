package com.example.newta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.newta.model.location
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var etLat: EditText
    private lateinit var etLong: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnRute: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().getReference("db")
        etLat = findViewById(R.id.etLat)
        etLong = findViewById(R.id.etLong)
        btnRute = findViewById(R.id.btnRute)
        btnAdd = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener {
            saveData()
        }
        btnRute.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
    }

    private fun saveData() {
        val lat = etLat.text.toString().trim()
        val long = etLong.text.toString().trim()
        if (lat.isEmpty() || long.isEmpty()){
            Toast.makeText(this, "isi field!", Toast.LENGTH_SHORT).show()
        }else
        {
            val routeId = database.push().key
            val STD = location(routeId.toString(),lat.toDouble(),long.toDouble() )
            database.child(routeId.toString()).setValue(STD)
            database.child(routeId.toString()).setValue(STD).addOnCompleteListener {
                Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()
            }
        }
    }
}