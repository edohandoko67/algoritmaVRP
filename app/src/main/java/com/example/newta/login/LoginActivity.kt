package com.example.newta.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newta.R
import com.example.newta.UI.MenuActivity
import com.example.newta.UI.user.UserActivity
import com.example.newta.chatbot.ChatActivity
import com.example.newta.model.login.LoginUser
import com.example.newta.model.login.SharePref
import com.example.newta.model.login.SharePref.Companion.key_level
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        supportActionBar?.hide()
        login_rl.visibility = View.VISIBLE
        signup_rl.visibility = View.GONE

        login_txt.setOnClickListener {
            login_txt.setTextColor(Color.parseColor("#8FE1A6"))
            signup_txt.setTextColor(Color.parseColor("#CBCBCB"))
            login_rl.visibility = View.VISIBLE
            signup_rl.visibility = View.GONE

        }

        signup_txt.setOnClickListener {
            signup_txt.setTextColor(Color.parseColor("#8FE1A6"))
            login_txt.setTextColor(Color.parseColor("#CBCBCB"))
            signup_rl.visibility = View.VISIBLE
            login_rl.visibility = View.GONE
        }
        btnLogin.setOnClickListener {
            val user = username_ET.text.toString().trim()
            val password = password_Et.text.toString().trim()
            if (user.isEmpty() || password.isEmpty()) {
                Toast.makeText(this.applicationContext, "Isi field!", Toast.LENGTH_SHORT).show()
            } else {
                val query = FirebaseDatabase.getInstance().getReference("/session/login/$user")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val sandi = snapshot.child("password").value.toString()
                                val level = snapshot.child("level").value.toString()
                                val status = snapshot.child("status").value.toString()
                                val sharedPref = SharePref(this@LoginActivity)
                                if (!sandi.isNullOrEmpty()) {
                                    if (status.equals("1")) {
                                        if (level.equals("admin")) {
                                            sharedPref.setSessionNIK("user", user)
                                            sharedPref.setSessionString(key_level, level)
                                            val intent =
                                                Intent(this@LoginActivity, MenuActivity::class.java)
                                            intent.putExtra("username", username_ET.text.toString())
                                            startActivity(intent)
                                            finish()
                                        }
                                    } else {
                                        sharedPref.setSessionNIK("user", user)
                                        sharedPref.setSessionString(key_level, level)
                                        val intent =
                                            Intent(this@LoginActivity, UserActivity::class.java)
                                        intent.putExtra("username", username_ET.text.toString())
                                        startActivity(intent)
                                        finish()
                                    }
                                }else {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Data tidak ditemukan",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        override fun onCancelled(error: DatabaseError) {
                            // Tidak perlu melakukan apa-apa saat onCancelled
                        }
                    })
            }
        }


        btnRegister.setOnClickListener {
            val username = username_regis_ET.text.toString().trim()
            val name = name_regis_ET.text.toString().trim()
            val address = address_regis_ET.text.toString().trim()
            val pass = password_regis_Et.text.toString().trim()
            if (username.isEmpty() || name.isEmpty() || address.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this.applicationContext, "Isi field ini!", Toast.LENGTH_SHORT).show()
            } else if (username.length < 6) {
                Toast.makeText(
                    this.applicationContext,
                    "username harus lebih dari 6 karakter",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (pass.length <6 ){
                Toast.makeText(
                    this.applicationContext,
                    "password anda kurang panjang",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else {
                val data = LoginUser(username, name, address, pass)
                FirebaseDatabase.getInstance().getReference("/session/login/$username")
                    .setValue(data)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this.applicationContext,
                            "Berhasil registrasi",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@LoginActivity, LoginActivity::class.java))
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this.applicationContext,
                            "Gagal registrasi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnCompleteListener {
                        finish()
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val sharePreference = SharePref(this)
        val level = sharePreference.getSessionString(key_level)
        if (level != null) {
            if (level.equals("admin")) {
                startActivity(Intent(this@LoginActivity, MenuActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@LoginActivity, UserActivity::class.java))
                finish()
            }
        }
    }
}