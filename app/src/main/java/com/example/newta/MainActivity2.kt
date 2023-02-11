package com.example.newta

import android.location.Location
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity2 : AppCompatActivity() {
    private lateinit var mMap: GoogleMap
    private lateinit var btnNearestNeighbor: Button
    private lateinit var locationList: List<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val database = Firebase.database.reference
        btnNearestNeighbor = findViewById(R.id.btn_nearest_neighbor)
        locationList = ArrayList()

        database.child("db").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<Data>()

                //Mengubah data dari Firebase menjadi list
                for (snapshot in dataSnapshot.children) {
                    val data = snapshot.getValue(Data::class.java)
                    dataList.add(data!!)
                }

                //Menggunakan algoritma nearest neighbor
                val nearest = nearestNeighbor(dataList)

                //Menampilkan data terdekat pada Google Maps
//                val markerOptions = MarkerOptions().position(nearest.latLng).title(nearest.title)
//                mMap.addMarker(markerOptions)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Menangani error saat mengambil data dari Firebase
            }
        })

    }

    private fun nearestNeighbor(dataList: List<Data>): Data {
        var nearestData = dataList[0]
        var nearestDistance = Float.MAX_VALUE

        //Mencari data terdekat
//        for (data in dataList) {
//            val distance = calculateDistance(currentLocation, data.latLng)
//            if (distance < nearestDistance) {
//                nearestData = data
//                nearestDistance = distance
//            }
//        }

        return nearestData
    }

    private fun calculateDistance(from: LatLng, to: LatLng): Float {
        val results = floatArrayOf(0f)
        Location.distanceBetween(from.latitude, from.longitude, to.latitude, to.longitude, results)
        return results[0]
    }
}

