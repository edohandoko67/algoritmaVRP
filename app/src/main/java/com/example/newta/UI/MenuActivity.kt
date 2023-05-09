package com.example.newta.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.newta.DatabaseActivity
import com.example.newta.MainActivity
import com.example.newta.MainActivity2
import com.example.newta.R
import com.example.newta.chatbot.ChatActivity
import com.example.newta.model.login.SharePref
import com.example.newta.model.login.SharePref.Companion.key_level
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        supportActionBar?.hide()
        val data = findViewById<ImageView>(R.id.imgData)
        val jadwal = findViewById<ImageView>(R.id.imgJadwal)
        val database = findViewById<ImageView>(R.id.imgDatabase)
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        val sharedPreferences = SharePref(this)
        val level = sharedPreferences.getSessionString(key_level)
        tvUser.text = level
        getPutExtra()

        data.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        jadwal.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
        database.setOnClickListener {
            startActivity(Intent(this, DatabaseActivity::class.java))
        }
        fab.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }
        ivUser.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }
    }

    private fun getPutExtra() {
        val data = intent.getStringExtra("username")
        tvUser.text = data
    }
}