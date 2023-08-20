package com.example.newta.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newta.R
import com.example.newta.UI.MenuActivity
import com.example.newta.UI.officer.OfficeActivity
import com.example.newta.UI.officer.OfficerActivity2
import com.example.newta.UI.officer.OfficerActivity3
import com.example.newta.UI.user.UserActivity
import com.example.newta.model.login.SessionManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        sessionManager = SessionManager(this)
        btnRegister.setOnClickListener {
            registerUser()
        }

        btnLogin.setOnClickListener {
            loginUser()
        }

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

    }

    private fun registerUser() {
        val name = names_regis_ET.text.toString().trim()
        val email = email_regis_ET.text.toString().trim()
        val password = password_regis_Et.text.toString().trim()

        if (name.isEmpty()) {
            names_regis_ET.error = "Isi field nama!"
            return
        }

        if (email.isEmpty()) {
            email_regis_ET.error = "Isi field email!"
            return
        }

        if (password.isEmpty()) {
            password_regis_Et.error = "Isi field password!"
            return
        }
        val isNewUser = checkIfUserIsNew()
        if (isNewUser){
            radio_admin.visibility = View.VISIBLE
            radio_officer.visibility = View.VISIBLE
        }

        btnRegister.setOnClickListener {
            val selectedRole = getUserRole(isNewUser)
            if (selectedRole == null) {
                Toast.makeText(this, "Pilih satu role!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else {

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val role = getUserRole(isNewUser)
                            addUserToFirestore(user, name, email, role)
                        } else {
                            Toast.makeText(
                                this,
                                "Registrasi gagal: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    private fun checkIfUserIsNew(): Boolean {
        val selectedRadioButtonId = role_radio_group.checkedRadioButtonId
        return selectedRadioButtonId == R.id.radio_user
    }


    private fun getUserRole(isNewUser: Boolean): List<String> {
        val selectedRoleId = role_radio_group.checkedRadioButtonId
        val role = mutableListOf<String>()

        when (selectedRoleId) {
            R.id.radio_admin -> {
                if (!isNewUser) {
                    role.add("admin")
                }
            }
            R.id.radio_officer -> {
                if (!isNewUser) {
                    role.add("officer")
                }
            }
            R.id.radio_user -> role.add("user")
        }

        // If no role is selected, return an empty list
        if (role.isEmpty()) {
            role.add("user")
        }

        return role
    }



    private fun addUserToFirestore(
        user: FirebaseUser?,
        name: String,
        email: String,
        roles: List<String>
    ) {
        user?.let {
            val userMap = hashMapOf(
                "name" to name,
                "email" to email,
                "roles" to roles
            )

            val userId = user.uid

            firestore.collection("users").document(userId)
                .set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Pendaftaran gagal: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun loginUser() {
        val email = email_ET.text.toString().trim()
        val password = password_Et.text.toString().trim()
        if (email.isEmpty()) {
            email_ET.error = "Isi field email!"
            return
        }

        if (password.isEmpty()) {
            password_Et.error = "Isi field password!"
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    sessionManager.setLoggedIn(true)
                    checkUserRole(user?.uid, email)
                } else {
                    Toast.makeText(
                        this,
                        "Login gagal: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun checkUserRole(userId: String?, email: String) {
        if (userId != null) {
                val firestore = FirebaseFirestore.getInstance()
                val userRef = firestore.collection("users").document(userId)

                userRef.get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val roles = document.get("roles") as? List<String>
                            val userName = document.getString("name")
                            sessionManager.setUserName(userName!!)
                            if (roles != null) {
                                if (roles.contains("admin")) {
                                    sessionManager.setUserRole("admin")
                                    startActivity(Intent(this, MenuActivity::class.java))
                                } else if (roles.contains("officer")) {
                                    sessionManager.setUserRole("officer")
                                    startActivity(Intent(this, OfficeActivity::class.java))
                                } else if (roles.contains("user")) {
                                    sessionManager.setUserRole("user")
                                    startActivity(Intent(this, UserActivity::class.java))
                                }else if (roles.contains("officer2")) {
                                    sessionManager.setUserRole("officer2")
                                    startActivity(Intent(this, OfficerActivity2::class.java))
                                }else if (roles.contains("officer3")){
                                    sessionManager.setUserRole("officer3")
                                    startActivity(Intent(this, OfficerActivity3::class.java))
                                }
                                finish()
                            } else {
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                                Toast.makeText(
                                    this,
                                    "User does not have valid roles",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                        }
                        val userEmail = email
                        sessionManager.setLoggedIn(true)
                        sessionManager.setUserEmail(userEmail)

                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            this,
                            "Failed to access Firestore: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
        } else {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show()
        }
    }
}














//        btnLogin.setOnClickListener {
//            val user = username_ET.text.toString().trim()
//            val password = password_Et.text.toString().trim()
//            if (user.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this.applicationContext, "Isi field!", Toast.LENGTH_SHORT).show()
//            } else {
//                val query = FirebaseDatabase.getInstance().getReference("/session/login/$user")
//                    .addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            if (snapshot.exists()) {
//                                val sandi = snapshot.child("password").value.toString()
//                                val level = snapshot.child("level").value.toString()
//                                val status = snapshot.child("status").value.toString()
//                                val sharedPref = SharePref(this@LoginActivity)
//                                if (!sandi.isNullOrEmpty()) {
//                                    if (status.equals("1")) {
//                                        if (level.equals("admin")) {
//                                            sharedPref.setSessionNIK("user", user)
//                                            sharedPref.setSessionString(key_level, level)
//                                            val intent =
//                                                Intent(this@LoginActivity, MenuActivity::class.java)
//                                            intent.putExtra("username", username_ET.text.toString())
//                                            startActivity(intent)
//                                            finish()
//                                        }
//                                    } else if (status.equals("2")) {
//                                        if (level.equals("officer")) {
//                                            sharedPref.setSessionNIK("user", user)
//                                            sharedPref.setSessionString(key_level, level)
//                                            val intent = Intent(this@LoginActivity, OfficeActivity::class.java)
//                                            intent.putExtra("username", username_ET.text.toString())
//                                            startActivity(intent)
//                                            finish()
//                                        }
//                                    }
//                                    else {
//                                        sharedPref.setSessionNIK("user", user)
//                                        sharedPref.setSessionString(key_level, level)
//                                        val intent =
//                                            Intent(this@LoginActivity, UserActivity::class.java)
//                                        intent.putExtra("username", username_ET.text.toString())
//                                        startActivity(intent)
//                                        finish()
//                                    }
//                                }else {
//                                        Toast.makeText(
//                                            this@LoginActivity,
//                                            "Data tidak ditemukan",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                }
//                            }
//                        override fun onCancelled(error: DatabaseError) {
//                            // Tidak perlu melakukan apa-apa saat onCancelled
//                        }
//                    })
//            }
//        }
//
//
//        btnRegister.setOnClickListener {
//            val username = username_regis_ET.text.toString().trim()
//            val name = name_regis_ET.text.toString().trim()
//            val address = address_regis_ET.text.toString().trim()
//            val pass = password_regis_Et.text.toString().trim()
//            if (username.isEmpty() || name.isEmpty() || address.isEmpty() || pass.isEmpty()) {
//                Toast.makeText(this.applicationContext, "Isi field ini!", Toast.LENGTH_SHORT).show()
//            } else if (username.length < 6) {
//                Toast.makeText(
//                    this.applicationContext,
//                    "username harus lebih dari 6 karakter",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            else if (pass.length <6 ){
//                Toast.makeText(
//                    this.applicationContext,
//                    "password anda kurang panjang",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            else {
//                val data = LoginUser(username, name, address, pass)
//                FirebaseDatabase.getInstance().getReference("/session/login/$username")
//                    .setValue(data)
//                    .addOnSuccessListener {
//                        Toast.makeText(
//                            this.applicationContext,
//                            "Berhasil registrasi",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        startActivity(Intent(this@LoginActivity, LoginActivity::class.java))
//                    }
//                    .addOnFailureListener {
//                        Toast.makeText(
//                            this.applicationContext,
//                            "Gagal registrasi",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    .addOnCompleteListener {
//                        finish()
//                    }
//            }
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        val sharePreference = SharePref(this)
//        val level = sharePreference.getSessionString(key_level)
//        if (level != null) {
//            if (level.equals("admin")) {
//                startActivity(Intent(this@LoginActivity, MenuActivity::class.java))
//                finish()
//            } else if (level.equals("officer")) {
//                startActivity(Intent(this@LoginActivity, OfficeActivity::class.java))
//                finish()
//            }
//            else {
//                startActivity(Intent(this@LoginActivity, UserActivity::class.java))
//                finish()
//            }
//        }
//    }
