package com.example.horoscopeapp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    companion object {
        const val  EXTRA_HOROSCOPE_ID = "HOROSCOPE_ID"
    }
    lateinit var horoscope: Horoscope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val id = intent.getStringExtra("EXTRA_HOROSCOPE_ID")
        horoscope = HoroscopeProvider.findById(id!!)!!

        findViewById<TextView>(R.id.textView).setText(horoscope.name)
        findViewById<ImageView>(R.id.imageView).setImageResource(horoscope.logo)
        var currentDay = LocalDate.now()
        var currentDayEn= currentDay.toString()
        findViewById<TextView>(R.id.today).setText(currentDayEn)
        if (esIdiomaEspanol()) {
            var currentDayStr = currentDay.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            findViewById<TextView>(R.id.today).setText(currentDayStr.toString())
        }
    }


    // Esta función devuelve el código de idioma actual del dispositivo.
    fun obtenerIdiomaDispositivo(): String {
        return Locale.getDefault().language
    }

    // Esta función devuelve true si el idioma actual es español.
    fun esIdiomaEspanol(): Boolean {
        return obtenerIdiomaDispositivo() == "es"
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_favorite -> {
                Log.i("MENU", "He hecho click en el menu de favorito")
                true
            }
            R.id.menu_share -> {
                Log.i("MENU", "He hecho click en el menu de compartir")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}