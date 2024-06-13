package com.example.horoscopeapp

import android.os.Bundle
import android.text.format.DateFormat
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    companion object {
        const val  EXTRA_HOROSCOPE_ID = "HOROSCOPE_ID"
    }
    lateinit var horoscope: Horoscope
    lateinit var currentDay: Date
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        currentDay = Date()

        // Esta función devuelve el código de idioma actual del dispositivo.
        fun obtenerIdiomaDispositivo(): String {
            return Locale.getDefault().language
        }

        // Esta función devuelve true si el idioma actual es español.
        fun esIdiomaEspanol(): Boolean {
            return obtenerIdiomaDispositivo() == "es"
        }

        // Comprobamos si el idioma del dispositivo es español.
        if (esIdiomaEspanol()) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            // Formateamos la fecha actual al formato español.
            val currentDay = LocalDate.now().format(formatter)
            // Ahora puedes usar 'fechaFormateada' donde necesites la fecha en el formato español.
        }


        val id = intent.getStringExtra("EXTRA_HOROSCOPE_ID")
        horoscope = HoroscopeProvider.findById(id!!)!!

        findViewById<TextView>(R.id.textView).setText(horoscope.name)
        findViewById<ImageView>(R.id.imageView).setImageResource(horoscope.logo)
        findViewById<TextView>(R.id.today).setText(currentDay.toString())
    }
}