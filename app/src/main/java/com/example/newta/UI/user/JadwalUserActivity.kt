package com.example.newta.UI.user

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.newta.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places


class JadwalUserActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal_user)
        supportActionBar?.hide()
        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val value = ai.metaData["com.google.android.geo.API_KEY"]
        val apiKey = value.toString()
        Log.i("APIKEY", apiKey)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            // Akses lokasi sudah diberikan
            setupMap()
        } else {
            // Minta izin akses lokasi
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
//        val database = Firebase.database.reference
//        database.child("db").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val dataList = mutableListOf<LatLngModel>()
////              Mengubah data dari Firebase menjadi list
//                for (snapshot in dataSnapshot.children) {
//                    val data = snapshot.getValue<LatLngModel>()
//                    dataList.add(data!!)
//                }
//
//                //Menggunakan algoritma nearest neighbor
//                val nearest = nearestNeighbor(dataList)
//
//                //Menampilkan data terdekat pada Google Maps
//                for (nearestData in nearest) {
//                    val data = LatLng(nearestData.latitude, nearestData.longitude)
//                    val title = nearestData.name
//                    val markerOptions = MarkerOptions().position(data).title(title)
//                    mMap.addMarker(markerOptions)
//                }
//                Log.i("Waypoint", nearest.toString())
//
//                if (nearest.size < 2) {
//                    Toast.makeText(
//                        applicationContext,
//                        "Data anda hanya terdapat satu lokasi saja, mohon untuk menambah lokasi tujuan anda!",
//                        Toast.LENGTH_LONG
//                    ).show()
//                } else {
//                    val originLocation = LatLng(nearest[0].latitude, nearest[0].longitude)
//                    val destinationLocation = LatLng(
//                        nearest[nearest.size - 1].latitude,
//                        nearest[nearest.size - 1].longitude
//                    )
//                    nearest.removeLast()
//                    nearest.removeFirst()
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 13F))
//                }
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//        })
//    }
//    private fun nearestNeighbor(locations: List<LatLngModel>): MutableList<LatLngModel> {
//        val result = mutableListOf<LatLngModel>()
//        for (i in locations.indices) {
//            var closestLocation = locations[0]
//            var closestDistance = distance(locations[i], closestLocation)
//            for (j in locations.indices) {
//                if (j == i) continue
//                val distance = distance(locations[i], locations[j])
//                if (distance < closestDistance) {
//                    closestLocation = locations[j]
//                    closestDistance = distance
//                }
//            }
//            result.add(locations[i])
//        }
//        return result
//    }
//
//    private fun distance(locationA: LatLngModel, locationB: LatLngModel): Double {
//        val earthRadius = 6371.0 // km
//        val latDistance = Math.toRadians(locationB.latitude - locationA.latitude)
//        val lngDistance = Math.toRadians(locationB.longitude - locationA.longitude)
//        val a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//                + (Math.cos(Math.toRadians(locationA.latitude)) * Math.cos(Math.toRadians(locationB.latitude))
//                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2)))
//        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
//        return earthRadius * c
//    }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin akses lokasi diberikan
                setupMap()
            } else {
                // Izin akses lokasi ditolak
                Toast.makeText(this, "Izin akses lokasi ditolak.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            // Izin akses lokasi diberikan
            mMap.isMyLocationEnabled = true

            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Dapatkan lokasi saat ini
                    location?.let {
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                        val markerOptions = MarkerOptions()
                            .position(currentLatLng)
                            .title("Lokasi Saya")

                        mMap.addMarker(markerOptions)

                        val latLngTextView = findViewById<TextView>(R.id.latLngTextView)
                        latLngTextView.visibility = View.VISIBLE
                        latLngTextView.text = "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
                    }
                }
        } else {
            // Izin akses lokasi ditolak
            Toast.makeText(this, "Izin akses lokasi ditolak.", Toast.LENGTH_SHORT).show()
        }
    }
}