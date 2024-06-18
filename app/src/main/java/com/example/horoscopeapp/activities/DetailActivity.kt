package com.example.horoscopeapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.horoscopeapp.data.Horoscope
import com.example.horoscopeapp.data.HoroscopeProvider
import com.example.horoscopeapp.R
import com.example.horoscopeapp.utils.SessionManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    companion object {
        const val  EXTRA_HOROSCOPE_ID = "HOROSCOPE_ID"
    }
    lateinit var horoscope: Horoscope
    var isFavorite = false

    lateinit var imageView: ImageView
    lateinit var textView: TextView

    lateinit var favoriteMenuItem: MenuItem

    lateinit var session: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        session = SessionManager(this)

        val id: String = intent.getStringExtra("EXTRA_HOROSCOPE_ID")!!
        horoscope = HoroscopeProvider.findById(id!!)!!
        isFavorite = session.isFavorite(horoscope.id)

        textView = findViewById(R.id.textView)
        imageView = findViewById(R.id.imageView)

        textView.setText(horoscope.name)
        imageView.setImageResource(horoscope.logo)

        supportActionBar?.setTitle(horoscope.name)
        supportActionBar?.setSubtitle(horoscope.description)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //CurrentDay Default English
        var currentDay = LocalDate.now()
        var currentDayEn= currentDay.toString()
        findViewById<TextView>(R.id.today).setText(currentDayEn)
        if (esIdiomaEspanol()) {
            var currentDayStr = currentDay.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            findViewById<TextView>(R.id.today).setText(currentDayStr.toString())
        }
    }

    fun setFavoriteIcon () {
        if (isFavorite) {
            favoriteMenuItem.setIcon(R.drawable.ic_favorite_selected)
        } else {
            favoriteMenuItem.setIcon(R.drawable.ic_favorite)
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
        favoriteMenuItem = menu.findItem(R.id.menu_favorite)
        setFavoriteIcon()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_favorite -> {
                if (isFavorite) {
                    session.setFavoriteHoroscope("")
                } else {
                    session.setFavoriteHoroscope(horoscope.id)
                }
                isFavorite = !isFavorite
                setFavoriteIcon()
                true
            }
            R.id.menu_share -> {
                val sendIntent = Intent()
                sendIntent.setAction(Intent.ACTION_SEND)
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                sendIntent.setType("text/plain")

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}