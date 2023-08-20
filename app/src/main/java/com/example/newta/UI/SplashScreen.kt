package com.example.newta.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newta.R
import com.example.newta.UI.officer.OfficeActivity
import com.example.newta.UI.user.UserActivity
import com.example.newta.login.LoginActivity
import com.example.newta.model.login.SessionManager
import com.example.newta.started.GetStartedActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class SplashScreen : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        sessionManager = SessionManager(this)
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000L)
            val userRole = sessionManager.getUserRole()
            if (sessionManager.isLoggedIn() && !userRole.isNullOrEmpty()) {
                if (userRole == "admin") {
                    startActivity(Intent(this@SplashScreen, MenuActivity::class.java))
                } else if (userRole == "officer") {
                    startActivity(Intent(this@SplashScreen, OfficeActivity::class.java))
                } else if (userRole == "user") {
                    startActivity(Intent(this@SplashScreen, UserActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                }
                finish()
            }
            else {
                startActivity(Intent(this@SplashScreen, GetStartedActivity::class.java))
                finish()
            }
        }
    }
}