package com.example.horoscopeapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.horoscopeapp.data.Horoscope
import com.example.horoscopeapp.data.HoroscopeProvider
import com.example.horoscopeapp.R
import com.example.horoscopeapp.utils.SessionManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailActivity : AppCompatActivity() {
    companion object {
        const val  EXTRA_HOROSCOPE_ID = "HOROSCOPE_ID"
    }
    lateinit var horoscope: Horoscope
    var isFavorite = false

    lateinit var imageView: ImageView
    lateinit var textView: TextView
    lateinit var userName: TextView

    lateinit var favoriteMenuItem: MenuItem

    lateinit var session: SessionManager

    lateinit var dailyHoroscopeTextView: TextView
    lateinit var progressBar: ProgressBar
    lateinit var closeButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        session = SessionManager(this)

        val id: String = intent.getStringExtra("EXTRA_HOROSCOPE_ID")!!
        horoscope = HoroscopeProvider.findById(id!!)!!
        isFavorite = session.isFavorite(horoscope.id)

        textView = findViewById(R.id.textView)
        imageView = findViewById(R.id.imageView)
        userName = findViewById(R.id.userName)
        closeButton = findViewById(R.id.closeButton)

        userName.text = session.getUserName()

        dailyHoroscopeTextView = findViewById(R.id.dailyHoroscopeTextView)
        progressBar = findViewById(R.id.progressBar)

        textView.setText(horoscope.name)
        imageView.setImageResource(horoscope.logo)

        supportActionBar?.setTitle(horoscope.name)
        supportActionBar?.setSubtitle(horoscope.description)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getDailyHoroscope()

        //CurrentDay Default English
        var currentDay = LocalDate.now()
        var currentDayEn= currentDay.toString()
        findViewById<TextView>(R.id.today).setText(currentDayEn)
        if (esIdiomaEspanol()) {
            var currentDayStr = currentDay.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            findViewById<TextView>(R.id.today).setText(currentDayStr.toString())
        }

        //Dejo preparado un botón para cerrar, pero según los manuales de usabilidad no es recomendable ponerlo.
        //lo dejo puesto más que nada como ejemplo.
        closeButton.setOnClickListener {
            finishAffinity()
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

    fun getDailyHoroscope() {
       progressBar.visibility = View.VISIBLE
        // Llamada en hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Declaramos la url
                val url = URL("https://horoscope-app-api.vercel.app/api/v1/get-horoscope/daily?sign=${horoscope.id}&day=TODAY")
                val con = url.openConnection() as HttpsURLConnection
                con.requestMethod = "GET"
                val responseCode = con.responseCode
                Log.i("HTTP", "Response Code :: $responseCode")

                // Preguntamos si hubo error o no
                if (responseCode == HttpsURLConnection.HTTP_OK) { // Ha ido bien
                    // Metemos el cuerpo de la respuesta en un BurfferedReader
                    val bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
                    var inputLine: String?
                    val response = StringBuffer()
                    while (bufferedReader.readLine().also { inputLine = it } != null) {
                        response.append(inputLine)
                    }
                    bufferedReader.close()

                    // Parsear JSON
                    val json = JSONObject(response.toString())
                    val result =  json.getJSONObject("data").getString("horoscope_data")

                    // Ejecutamos en el hilo principal
                    /*CoroutineScope(Dispatchers.Main).launch {

                    }*/
                    runOnUiThread {
                        Log.i("verH",result.toString())
                        dailyHoroscopeTextView.text = result
                        session.setInfoHoroscope(result.toString())
                        progressBar.visibility = View.GONE
                    }

                } else { // Hubo un error
                    Log.w("HTTP", "Response :: Hubo un error")
                }
            } catch (e: Exception) {
                Log.e("HTTP", "Response Error :: ${e.stackTraceToString()}")
            }
        }
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
                sendIntent.putExtra(Intent.EXTRA_TEXT, session.getInfoHoroscope())
                sendIntent.setType("text/plain")

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}