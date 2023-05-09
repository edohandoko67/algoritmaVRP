package com.example.newta.started

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newta.R
import com.example.newta.login.LoginActivity
import kotlinx.android.synthetic.main.activity_get_started.*

class GetStartedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        supportActionBar?.hide()
        btnStarted.setOnClickListener {
            startActivity(Intent(this.applicationContext, LoginActivity::class.java))
        }
    }
}