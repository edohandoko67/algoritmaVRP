package com.example.newta.UI

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newta.R
import com.example.newta.login.LoginActivity
import com.example.newta.model.login.SharePref
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        btnLogout.setOnClickListener {
            val sharedPref = SharePref(this)
            sharedPref.removeData()
            startActivity(Intent(this@InfoActivity, LoginActivity::class.java))
        }
    }
}