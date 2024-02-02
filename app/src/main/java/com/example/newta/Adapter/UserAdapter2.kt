package com.example.newta.Adapter

import android.app.AlertDialog
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
import com.example.newta.model.LatLngModel
import com.google.firebase.database.FirebaseDatabase

class UserAdapter2(val userList: List<LatLngModel>, val context: Context) : RecyclerView.Adapter<UserAdapter2.Holder>(){
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val username = itemView.findViewById(R.id.textViewOfficer) as TextView
        val lat = itemView.findViewById(R.id.textViewOfficer2) as TextView
        val long = itemView.findViewById(R.id.textViewOfficer3) as TextView
        val size = itemView.findViewById(R.id.textViewOfficer4) as TextView
        val edit = itemView.findViewById(R.id.editimageoff) as ImageView
        val delete = itemView.findViewById(R.id.deleteimageoff) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_officer, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.username.text = userList.get(position).name
        holder.lat.text = userList.get(position).latitude.toString()
        holder.long.text = userList.get(position).longitude.toString()
        holder.size.text = userList.get(position).capacity.toString()
        holder.edit.setOnClickListener {
            val perItemPosition = userList.get(position)
            updateDialog(perItemPosition)
        }
        holder.delete.setOnClickListener {
            val perItemPosition = userList.get(position)
            deleteData(perItemPosition.user_id)
        }
    }

    private fun updateDialog(perItemPosition: LatLngModel) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.update_dialog_officer, null)
        builder.setCancelable(false)

        val name = view.findViewById<EditText>(R.id.editTextOfficer)
        val lat = view.findViewById<EditText>(R.id.updateLatOfficer)
        val long = view.findViewById<EditText>(R.id.updateLongOfficer)
        val size = view.findViewById<EditText>(R.id.updateNumberOfficer)

        name.setText(perItemPosition.name)
        lat.setText(perItemPosition.latitude.toString())
        long.setText(perItemPosition.longitude.toString())
        size.setText(perItemPosition.capacity.toString())

        builder.setView(view)
        builder.setPositiveButton("Update", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val users = FirebaseDatabase.getInstance().getReference("db2")
                val names = name.text.toString()
                val lats = lat.text.toString().toDouble()
                val longs = long.text.toString().toDouble()
                val sizes = size.text.toString().toInt()

                if (names.isEmpty() && lats.isNaN() && longs.isNaN() ){
                    name.error = "Isi data semua"
                    name.requestFocus()
                    return
                }else {
                    val std_data = LatLngModel(perItemPosition.user_id, lats, longs, names, sizes)
                    users.child(perItemPosition.user_id).setValue(std_data)
                    Toast.makeText(context, "Data Uploaded", Toast.LENGTH_SHORT).show()
                }
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
    private fun deleteData(user_id: String){
        val users = FirebaseDatabase.getInstance().getReference("db2").child(user_id)
        users.removeValue().addOnCompleteListener(){
            Toast.makeText(context, "Data Deleted Successfully", Toast.LENGTH_SHORT).show()
        }
    }
}