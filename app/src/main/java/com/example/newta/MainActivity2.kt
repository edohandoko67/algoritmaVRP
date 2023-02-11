package com.example.newta

import android.location.Location
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.newta.model.LatLngModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class MainActivity2 : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var btnNearestNeighbor: Button
    private lateinit var locationList: List<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val database = Firebase.database.reference
        btnNearestNeighbor = findViewById(R.id.btn_nearest_neighbor)
        locationList = ArrayList()

        database.child("db").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<LatLngModel>()
//              Mengubah data dari Firebase menjadi list
                for (snapshot in dataSnapshot.children) {
                    val data = snapshot.getValue<LatLngModel>()
                    dataList.add(data!!)
                }

                //Menggunakan algoritma nearest neighbor
                val nearest = nearestNeighbor(dataList)

                //Menampilkan data terdekat pada Google Maps
                for (nearestData in nearest) {
                    val data = LatLng(nearestData.latitude, nearestData.longitude)
                    val title = nearestData.id
                    val markerOptions = MarkerOptions().position(data).title(title)
                    mMap.addMarker(markerOptions)
                }
                val originLocation = LatLng(nearest[0].latitude, nearest[0].longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))

            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Menangani error saat mengambil data dari Firebase
            }
        })

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun nearestNeighbor(locations: List<LatLngModel>): List<LatLngModel> {
        val result = mutableListOf<LatLngModel>()
        for (i in locations.indices) {
            var closestLocation = locations[0]
            var closestDistance = distance(locations[i], closestLocation)
            for (j in locations.indices) {
                if (j == i) continue
                val distance = distance(locations[i], locations[j])
                if (distance < closestDistance) {
                    closestLocation = locations[j]
                    closestDistance = distance
                }
            }
            result.add(locations[i])
        }
        return result
    }

    private fun distance(locationA: LatLngModel, locationB: LatLngModel): Double {
        val earthRadius = 6371.0 // km
        val latDistance = Math.toRadians(locationB.latitude - locationA.latitude)
        val lngDistance = Math.toRadians(locationB.longitude - locationA.longitude)
        val a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + (Math.cos(Math.toRadians(locationA.latitude)) * Math.cos(Math.toRadians(locationB.latitude))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2)))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadius * c
    }


//    private fun calculateDistance(a: LatLngModel, b: LatLngModel): Double {
//        val xDiff = a.latitude - b.latitude
//        val yDiff = a.longitude - b.longitude
//        return Math.sqrt(xDiff * xDiff + yDiff * yDiff)
//    }
//
//    private fun nearestNeighbor(dataList: List<LatLngModel>): List<LatLngModel> {
//        var nearestData = dataList[0]
//        var nearestDistance = Float.MAX_VALUE
//
//        //Mencari data terdekat
//        for (data in dataList) {
//            val distance = calculateDistance(currentLocation, data.latLng)
//            if (distance < nearestDistance) {
//                nearestData = data
//                nearestDistance = distance
//            }
//        }
//
//        return nearestData
//    }

//    private fun nearestNeighbor(coord: LatLngModel, coordinates: List<LatLngModel>): List<LatLngModel> {
//        return coordinates
//            .map { Pair(it, calculateDistance(coord, it)) }
//            .sortedBy { it.second }
//            .map { it.first }
//    }

//    private fun calculateDistance(from: LatLng, to: LatLng): Float {
//        val results = floatArrayOf(0f)
//        Location.distanceBetween(from.latitude, from.longitude, to.latitude, to.longitude, results)
//        return results[0]
//    }
}

