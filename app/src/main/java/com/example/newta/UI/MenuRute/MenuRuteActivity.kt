package com.example.newta.UI.MenuRute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newta.R.layout.*
import com.example.newta.UI.MenuRute.Rute.*
import kotlinx.android.synthetic.main.activity_menu_rute.*

class MenuRuteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_menu_rute)
        supportActionBar?.hide()

        cardViewRuteA.setOnClickListener {
            startActivity(Intent(this, RuteAParseActivity::class.java))
        }

        cardViewRuteB.setOnClickListener {
            startActivity(Intent(this, RuteBParseActivity::class.java))
        }

        cardViewRuteC.setOnClickListener {
            startActivity(Intent(this, DummyActivity3::class.java))
        }

        cardViewRuteD.setOnClickListener {
            startActivity(Intent(this, RuteCParseActivity::class.java))
        }

        cardViewRuteE.setOnClickListener {
            startActivity(Intent(this, RuteDParseActivity::class.java))
        }

        cardViewRuteF.setOnClickListener {
            startActivity(Intent(this, RuteEParseActivity::class.java))
        }

        cardViewRuteG.setOnClickListener {
            startActivity(Intent(this, RuteFParseActivity::class.java))
        }

        cardViewRuteH.setOnClickListener {
            startActivity(Intent(this, RuteGParseActivity::class.java))
        }
    }
}