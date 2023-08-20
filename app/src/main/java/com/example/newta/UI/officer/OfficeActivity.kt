package com.example.newta.UI.officer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newta.activity.MainActivity2
import com.example.newta.R
import com.example.newta.UI.InfoActivity
import com.example.newta.model.login.Constant
import com.example.newta.model.login.PreferenceHelper
import com.example.newta.model.login.SessionManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_office.*

class OfficeActivity : AppCompatActivity() {
    private lateinit var databaseReff: DatabaseReference
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_office)
        supportActionBar?.hide()

        sessionManager = SessionManager(this)
        val username = sessionManager.getUserEmail()
        tvUserOff.text = username

        databaseReff = FirebaseDatabase.getInstance().reference
        ivUserOff.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }
        cardViewDataOff.setOnClickListener {
            startActivity(Intent(this, AddRuteOffActivity::class.java))
        }
        cardViewRuteOff.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
        getTotal()
    }

    private fun getTotal() {
        databaseReff.child("db").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalCap = 0
                for (dataSnap in snapshot.children){
                    val capacitys = dataSnap.child("capacity").getValue(Int::class.java)
                    if (capacitys != null) {
                        totalCap += capacitys
                    }
                    tvSizeOff.text = "$totalCap kg"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO()
            }
        })
    }
}