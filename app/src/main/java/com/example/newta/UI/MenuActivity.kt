package com.example.newta.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import com.example.newta.R
import com.example.newta.UI.MenuRute.MenuRuteActivity
import com.example.newta.activity.*
import com.example.newta.model.login.Constant
import com.example.newta.model.login.SessionManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    private lateinit var databaseRef: DatabaseReference
    private lateinit var sessionManager: SessionManager
    private lateinit var spinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar?.hide()

        val datas = listOf("db", "db2", "db3")
        val maps = listOf("maps", "maps2", "maps3")
        val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, datas)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, maps)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner1 = findViewById<Spinner>(R.id.spinnerOptions)
        spinner1.adapter = adapter1

        val spinner2 = findViewById<Spinner>(R.id.spinnerMaps)
        spinner2.adapter = adapter2
        // Apply the adapter to the spinner
        databaseRef = FirebaseDatabase.getInstance().reference
        sessionManager = SessionManager(this)
        val tvUser = findViewById<TextView>(R.id.tvUser)
        val username = sessionManager.getUserEmail()
        tvUser.text = username
        val data = findViewById<ImageView>(R.id.imgData)
        val jadwal = findViewById<ImageView>(R.id.imgJadwal)
        val database = findViewById<ImageView>(R.id.imgDatabase)
        getTotalCapacityFromFirebase()

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = datas[position]
                when (selectedItem) {
                    "db" -> {
                        val intent = Intent(this@MenuActivity, DatabaseActivity::class.java)
                        startActivity(intent)
                    }
                    "db2" -> {
                        val intent = Intent(this@MenuActivity, DatabaseActivityOfficer::class.java)
                        startActivity(intent)
                    }
                    "db3" -> {
                        val intent = Intent(this@MenuActivity, DatabaseActivityOfficer2::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = maps[position]
                when (selectedItem) {
                    "maps" -> {
                        val intent = Intent(this@MenuActivity, MainActivity2::class.java)
                        startActivity(intent)
                    }
                    "maps2" -> {
                        val intent = Intent(this@MenuActivity, MainActivity3::class.java)
                        startActivity(intent)
                    }
                    "maps3" -> {
                        val intent = Intent(this@MenuActivity, MainActivity4::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        data.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        jadwal.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
        database.setOnClickListener {
            startActivity(Intent(this, DatabaseActivity::class.java))
        }
        ivUser.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }
        cardView5.setOnClickListener {
            startActivity(Intent(this, StoryActivity3::class.java ))
        }
        imgRute.setOnClickListener {
            startActivity(Intent(this, MenuRuteActivity::class.java))
        }
        imgInputData.setOnClickListener {
            startActivity(Intent(this, InputHistoryActivity3::class.java))
        }
    }

    private fun getTotalCapacityFromFirebase() {
        databaseRef.child("db").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var totalCapacity = 0
                for (snapshot in dataSnapshot.children) {
                    val capacity = snapshot.child("capacity").getValue(Int::class.java)
                    totalCapacity += capacity!!
                    tvSize.text = "$totalCapacity kg"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}