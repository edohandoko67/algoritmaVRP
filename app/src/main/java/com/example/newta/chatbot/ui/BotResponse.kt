package com.example.newta.chatbot.ui

import com.example.newta.chatbot.ui.Constants.OPEN_GOOGLE
import com.example.newta.chatbot.ui.Constants.OPEN_WHATSAPP
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object BotResponse {
    fun basicResponses(_message: String): Any {
        val random = (0..2).random()
        val message = _message.toLowerCase()

        return when {
            //Hello
            message.contains("hello") || message.contains("halo") || message.contains("hi") -> {
                when (random) {
                    0 -> "Hello there!"
                    1 -> "Sup"
                    2 -> "Halo!"
                    else -> "error"
                }
            }

            //How Are U
            message.contains("how are you") -> {
                when (random) {
                    0 -> "I'm fine, thanks for asking!"
                    1 -> "I'm hungry!"
                    2 -> "I'm good!, How about you?"
                    else -> "error"
                }
            }

            message.contains("time") && message.contains("?") -> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdf.format(Date(timeStamp.time))
                date.toString()
            }

            message.contains("petunjuk") -> {
                "1. Sampah kecil warna hijau untuk ukuran 3kg" +
                "\n2. Sampah sedang warna biru untuk ukuran 5kg" +
                "\n3. Sampah besar warna merah untuk ukuran 10kg"
            }

            message.contains("whatsapp") || message.contains("wa") -> {
                OPEN_WHATSAPP
            }

            message.contains("open") || message.contains("google") -> {
                OPEN_GOOGLE
            }

            else -> {
                when (random) {
                    0 -> "I don't understand"
                    1 -> "I don't know"
                    2 -> "Try again!"
                    else -> "error"
                }
            }
        }
    }
}
