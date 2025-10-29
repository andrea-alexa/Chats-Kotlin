package com.example.chats_kotlin

import java.util.Calendar
import java.util.Locale

object Constantes {
    fun obtenerTiempoDelD(): Long{
        return System.currentTimeMillis()
    }

    fun formatoFecha(tiempo: Long) :String{
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = tiempo

        return android.text.format.DateFormat.format("dd-MM-yyyy", calendar).toString()
    }
}