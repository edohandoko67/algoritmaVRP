package com.example.newta.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.newta.R
import com.example.newta.login.LoginActivity
import com.example.newta.model.login.Constant
import com.example.newta.model.login.PreferenceHelper
import com.example.newta.model.login.SessionManager
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        supportActionBar?.hide()

        sessionManager = SessionManager(this)
        val name = sessionManager.getUserName()
        val email = sessionManager.getUserEmail()
        tvEmailPengguna.text = email
        tvNamaPengguna.text = name
        btnLogout.setOnClickListener {
            logoutUser()
        }
    }
    private fun logoutUser() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Logout?")
        alertDialogBuilder.setMessage("Anda yakin ingin keluar?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            sessionManager.setLoggedIn(false)
            sessionManager.logout()
            Toast.makeText(applicationContext, "Berhasil Logout", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@InfoActivity, LoginActivity::class.java))
            finish()
        }
        alertDialogBuilder.setNegativeButton("No"){ dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
