package com.example.newta.UI.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newta.MainActivity2
import com.example.newta.R
import com.example.newta.UI.InfoActivity
import com.example.newta.chatbot.ChatActivity
import com.example.newta.model.login.SharePref
import com.example.newta.model.login.SharePref.Companion.key_level
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        supportActionBar?.hide()
        imgUserJadwal.setOnClickListener {
            startActivity(Intent(this, JadwalUserActivity::class.java))
        }
        fabUser.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }
        ivUser2.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }

        val sharedPreferences = SharePref(this)
        val levelUser = sharedPreferences.getSessionString(key_level)
        tvUser2.text = levelUser
        getPutExtras()
    }

    private fun getPutExtras() {
        val data = intent.getStringExtra("username")
        tvUser2.text = data
    }
}