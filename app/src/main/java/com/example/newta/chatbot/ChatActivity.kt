package com.example.newta.chatbot

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newta.R
import com.example.newta.chatbot.data.Message
import com.example.newta.chatbot.ui.BotResponse
import com.example.newta.chatbot.ui.Constants.OPEN_GOOGLE
import com.example.newta.chatbot.ui.Constants.OPEN_WHATSAPP
import com.example.newta.chatbot.ui.Constants.RECEIVE_ID
import com.example.newta.chatbot.ui.Constants.SEND_ID
import com.example.newta.chatbot.ui.Time
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.*

class ChatActivity : AppCompatActivity() {
    private lateinit var adapter: MessagingAdapter
    var messagesList = mutableListOf<Message>()
    private val botList = listOf("Tiara", "Nadia", "Melissa")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar?.hide()

        recyclerView()
        clickEvents()
        val random = (0..2).random()
        customMessage("Hello! Hari ini kamu berbicara dengan ${botList[random]}, Ada yang bisa kami bantu?" +
                "Biar saya bantu sedikit untuk informasi produk kami, silakan ketik 'petunjuk' untuk mengetahuinya!" +
                "\nUntuk info lebih jelas nya anda ketikan perintah 'whatsapp'"
        )

    }

    private fun clickEvents() {
        btn_send.setOnClickListener {
            sendMessage()
        }

        et_message.setOnClickListener {
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    rv_messages.scrollToPosition(adapter.itemCount - 1)

                }
            }
        }
    }

    private fun customMessage(message: String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                val timeStamp = Time.timeStamp()
                messagesList.add(Message(message, RECEIVE_ID, timeStamp))
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))
                rv_messages.scrollToPosition(adapter.itemCount-1)
            }
        }
    }

    private fun recyclerView() {
        adapter = MessagingAdapter()
        rv_messages.adapter = adapter
        rv_messages.layoutManager = LinearLayoutManager(applicationContext)
    }

    //pengembalian
    private fun sendMessage(){
        val message = et_message.text.toString()
        val timeStamp = Time.timeStamp()
        if (message.isNotEmpty()){
            messagesList.add(Message(message, SEND_ID, timeStamp))
            et_message.setText("")
            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            rv_messages.scrollToPosition(adapter.itemCount-1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()
        GlobalScope.launch {
            delay(1000)

            withContext(Dispatchers.Main){
                //get response
                val response = BotResponse.basicResponses(message)
                messagesList.add(Message(response, RECEIVE_ID, timeStamp))
                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))
                rv_messages.scrollToPosition(adapter.itemCount-1)

                when(response){
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://wwww.google.com/")
                        startActivity(site)
                    }
                    OPEN_WHATSAPP -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://api.whatsapp.com/send?phone=081554767227")
                        startActivity(site)
                    }
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}