package com.example.newta.Adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.newta.R
import com.example.newta.model.StoryModel
import com.google.firebase.database.FirebaseDatabase
import okhttp3.internal.userAgent
import java.util.*

class StoryAdapter(val historyList: List<StoryModel>, val context: Context): RecyclerView.Adapter<StoryAdapter.Holder>() {
    class Holder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById(R.id.textViewWeek2) as TextView
        val total = itemView.findViewById(R.id.textResultTotal) as TextView
        val plat = itemView.findViewById(R.id.textResultPlat) as TextView
        val jarak = itemView.findViewById(R.id.textResultJarak) as TextView
        val time = itemView.findViewById(R.id.textResultWaktu) as TextView
        val bahan = itemView.findViewById(R.id.textResultTimeBahanBakar) as TextView
        val priceBahan = itemView.findViewById(R.id.textResultTimePriceBahanBakar) as TextView
        val date = itemView.findViewById(R.id.textResultTime) as TextView
        val edit = itemView.findViewById(R.id.editStory) as ImageView
        val delete = itemView.findViewById(R.id.deleteStory) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_story_layout, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.name.text = historyList.get(position).weekly
        holder.total.text = historyList.get(position).totalCapacity.toString()
        holder.plat.text = historyList.get(position).nopol
        holder.jarak.text = historyList.get(position).distance.toString()
        holder.time.text = historyList.get(position).time.toString()
        holder.bahan.text = historyList.get(position).bahan.toString()
        holder.priceBahan.text = historyList.get(position).price.toString()
        holder.date.text = historyList.get(position).date
        holder.edit.setOnClickListener {
            val perItemPosition = historyList[position]
            updateDialog(perItemPosition)
        }
        holder.delete.setOnClickListener {
            val perItemPosition = historyList[position]
            deleteDialog(perItemPosition.adminId)
        }

    }

    private fun deleteDialog(adminId: String) {
        val users = FirebaseDatabase.getInstance().getReference("histori").child(adminId)
        users.removeValue().addOnCompleteListener(){
            Toast.makeText(context, "Data Deleted Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateDialog(perItemPosition: StoryModel) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.update_story, null)
        builder.setCancelable(false)

        val name = view.findViewById<EditText>(R.id.updateWeek)
        val total = view.findViewById<EditText>(R.id.updateTotal)
        val date = view.findViewById<EditText>(R.id.updateTanggal)
        val plat = view.findViewById<EditText>(R.id.updatePlat)
        val jarak = view.findViewById<EditText>(R.id.updateJarak)
        val waktu = view.findViewById<EditText>(R.id.updateTime)
        val bahan = view.findViewById<EditText>(R.id.updateBahan)
        val price = view.findViewById<EditText>(R.id.updatePriceBahan)

        name.setText(perItemPosition.weekly)
        total.setText(perItemPosition.totalCapacity.toString())
        date.setText(perItemPosition.date)
        plat.setText(perItemPosition.nopol)
        jarak.setText(perItemPosition.distance.toString())
        waktu.setText(perItemPosition.time.toString())
        bahan.setText(perItemPosition.bahan.toString())
        price.setText(perItemPosition.price.toString())

        date.setOnClickListener {
            showDate(date)
        }
        builder.setView(view)
        builder.setPositiveButton("Update", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val user = FirebaseDatabase.getInstance().getReference("histori")
                val name = name.text.toString()
                val total = total.text.toString().toInt()
                val plat = plat.text.toString()
                val jarak = jarak.text.toString().toInt()
                val waktu = waktu.text.toString().toInt()
                val bahan = bahan.text.toString().toInt()
                val price = price.text.toString().toInt()
                val dates = date.text.toString()
                val std = StoryModel(perItemPosition.adminId, name, total, dates, plat, jarak, waktu, bahan, price)
                user.child(perItemPosition.adminId).setValue(std)
                Toast.makeText(context, "Data Updated", Toast.LENGTH_SHORT).show()
            }
        })
        builder.setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                return
            }
        })
        val alert = builder.create()
        alert.show()
    }

    private fun showDate(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            context,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
                editText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}