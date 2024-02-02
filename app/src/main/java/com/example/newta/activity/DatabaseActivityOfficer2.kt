package com.example.newta.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newta.Adapter.UserAdapter2
import com.example.newta.Adapter.UserAdapter3
import com.example.newta.R
import com.example.newta.model.LatLngModel
import com.google.firebase.database.*

class DatabaseActivityOfficer2 : AppCompatActivity() {
    lateinit var rute: DatabaseReference
    lateinit var routeList: MutableList<LatLngModel>
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_officer2)
        supportActionBar?.hide()
        recyclerView = findViewById(R.id.recyclerviewOff2)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemDecor = DividerItemDecoration(this, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(itemDecor)

        progressBar = findViewById(R.id.progressbar)
        rute = FirebaseDatabase.getInstance().getReference("db3")
        routeList = mutableListOf()

        LoadData()
    }

    private fun LoadData(){
        progressBar.visibility = View.VISIBLE
        rute.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    routeList.clear()

                    for (data in snapshot.children){
                        val std = data.getValue(LatLngModel::class.java)
                        routeList.add(std!!)
                    }

                    val adapter = UserAdapter3(routeList, this@DatabaseActivityOfficer2)
                    recyclerView.adapter = adapter
                    progressBar.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                }

                else {
                    Toast.makeText(applicationContext,"No data Found", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}