package com.example.newta.UI.officer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newta.R
import com.example.newta.UI.InfoActivity
import com.example.newta.activity.MainActivity3
import com.example.newta.model.login.SessionManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_officer3.*

class OfficerActivity3 : AppCompatActivity() {
    private lateinit var databaseReff: DatabaseReference
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_officer3)
        supportActionBar?.hide()

        sessionManager = SessionManager(this)
        val username = sessionManager.getUserEmail()
        tvUserOff3.text = username

        databaseReff = FirebaseDatabase.getInstance().reference
        ivUserOff3.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }
        cardViewDataOff3.setOnClickListener {
            startActivity(Intent(this, AddRuteOffActivity::class.java))
        }
        cardViewRuteOff3.setOnClickListener {
            startActivity(Intent(this, MainActivity3::class.java))
        }
        getTotals()
    }

    private fun getTotals() {
        databaseReff.child("db3").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalCap = 0
                for (dataSnap in snapshot.children){
                    val capacitys = dataSnap.child("capacity").getValue(Int::class.java)
                    if (capacitys != null) {
                        totalCap += capacitys
                    }
                    tvSizeOff3.text = "$totalCap kg"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO()
            }
        })
    }
}