package com.example.newta.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newta.Adapter.StoryAdapter
import com.example.newta.R
import com.example.newta.model.StoryModel
import com.google.firebase.database.*

class StoryActivity3 : AppCompatActivity() {
    lateinit var weekStory: DatabaseReference
    lateinit var wasteList: MutableList<StoryModel>
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story3)
        supportActionBar?.hide()

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemDecor = DividerItemDecoration(this, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(itemDecor)

        progressBar = findViewById(R.id.progressbar)
        weekStory = FirebaseDatabase.getInstance().getReference("histori")
        wasteList = mutableListOf()

        LoadDataWaste()
    }

    private fun LoadDataWaste() {
        progressBar.visibility = View.VISIBLE
        weekStory.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    wasteList.clear()

                    for (result in snapshot.children){
                        val storyModel = snapshot.getValue(StoryModel::class.java)
                       // val date = storyModel?.date
                        val stds = result.getValue(StoryModel::class.java)
                        wasteList.add(stds!!)
                    }

                    val adapter = StoryAdapter(wasteList, this@StoryActivity3)
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