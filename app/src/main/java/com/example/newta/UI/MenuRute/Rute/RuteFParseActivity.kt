package com.example.newta.UI.MenuRute.Rute

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newta.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_dummy3.*
import kotlinx.android.synthetic.main.activity_rute_cparse.*
import kotlinx.android.synthetic.main.activity_rute_fparse.*

class RuteFParseActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private val location = mutableListOf<LatLng>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rute_fparse)
        supportActionBar?.hide()

        mapViewF.onCreate(savedInstanceState)
        mapViewF.getMapAsync(this)

        val gmmIntentUri = Uri.parse(
            "https://www.google.com/maps/dir/-7.5368081,112.5445416/Jalan+Raya+Trawas+-+Mojosari/Jalan+Pattimura/Jalan+Imam+Bonjol/Jalan+Teuku+Umar/Jalan+Tribuana+Tungga+Dewi/Dlimo/Jalan+Raden+Patah/@-7.5379617,112.5320544,14z/data=!3m1!4b1!4m45!4m44!1m0!1m5!1m1!1s0x2e787444965d0659:0x703ed83cdc2adfff!2m2!1d112.5572984!2d-7.5572801!1m5!1m1!1s0x2e780b492924b587:0x83a9354fd59af949!2m2!1d112.5432656!2d-7.5156613!1m5!1m1!1s0x2e7874b0841e1ee7:0x43b88a9d625aa6d7!2m2!1d112.5408619!2d-7.5270896!1m5!1m1!1s0x2e7874cebe646b1d:0xa4ae5336e556275c!2m2!1d112.5551543!2d-7.5250802!1m5!1m1!1s0x2e780b48c7c3c505:0xc814eb8e1dc815d6!2m2!1d112.540587!2d-7.5198777!1m5!1m1!1s0x2e7874ed29c97617:0x2a922f8ee9930a9!2m2!1d112.5490939!2d-7.5479015!1m5!1m1!1s0x2e7874dac7dddf59:0x350621c64a3c6973!2m2!1d112.5662705!2d-7.5298454!3e0?entry=ttu"
        )
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        if (location.isNotEmpty()){
            val cameraPos = CameraPosition.builder()
                .target(location.first())
                .zoom(15f)
                .build()
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos))
            for (locations in location){
                mMap.addMarker(MarkerOptions().position(locations))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapViewF.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapViewF.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapViewF.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapViewF.onLowMemory()
    }
}