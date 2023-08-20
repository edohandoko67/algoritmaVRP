package com.example.newta.UI.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.newta.R
import com.example.newta.UI.InfoActivity
import com.example.newta.UI.officer.AddRuteOffActivity
import com.example.newta.chatbot.ChatActivity
import com.example.newta.model.login.Constant
import com.example.newta.model.login.PreferenceHelper
import com.example.newta.model.login.SessionManager
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        supportActionBar?.hide()

        val sessionManager = SessionManager(this)
        val username = sessionManager.getUserEmail()
        tvUser2.text = username

        imgUserJadwal.setOnClickListener {
            startActivity(Intent(this, JadwalUserActivity::class.java))
        }
        AddPenjemputan.setOnClickListener {
            startActivity(Intent(this, AddRuteOffActivity::class.java))
        }

        ivUser2.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }

    }
}




//    private fun sendWhatsAppMessage() {
//        val uri = Uri.parse("https://api.whatsapp.com/send?phone=+6281554767227")
//        val intent = Intent(Intent.ACTION_VIEW, uri)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//
//        // Pastikan aplikasi WhatsApp terinstal sebelum membuka intent
//        if (intent.resolveActivity(packageManager) != null) {
//            startActivity(intent)
//        }
//    }

