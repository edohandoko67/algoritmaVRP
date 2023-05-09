package com.example.newta

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newta.Adapter.UserAdapter
import com.example.newta.model.LatLngModel
import com.google.firebase.database.*

class DatabaseActivity : AppCompatActivity() {
    lateinit var rute: DatabaseReference
    lateinit var routeList: MutableList<LatLngModel>
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)
        supportActionBar?.hide()
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemDecor = DividerItemDecoration(this, VERTICAL)
        recyclerView.addItemDecoration(itemDecor)

        progressBar = findViewById(R.id.progressbar)
        rute = FirebaseDatabase.getInstance().getReference("db")
        routeList = mutableListOf()

        LoadData()
    }

    private fun LoadData(){
        progressBar.visibility = View.VISIBLE
        rute.addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    routeList.clear()

                    for (data in snapshot.children){
                        val std = data.getValue(LatLngModel::class.java)
                        routeList.add(std!!)
                    }

                    val adapter = UserAdapter(routeList, this@DatabaseActivity)
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