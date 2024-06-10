package com.example.horoscopeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    val horoscopeList: List<Horoscope> = listOf(
        Horoscope("aries", "Aries", 0),
        Horoscope("taurus", "Taurus", 0),
        Horoscope("gemini", "Gemini", 0),
        Horoscope("cancer", "Cancer", 0),
        Horoscope("leo", "Leo", 0),
        Horoscope("virgo", "Virgo", 0),
        Horoscope("libra", "Libra", 0),
        Horoscope("scorpio", "Scorpio", 0),
        Horoscope("sagittarius", "Sagittarius", 0),
        Horoscope("capricorn", "Capricorn", 0),
        Horoscope("aquarius", "Aquarius", 0),
        Horoscope("pisces", "Pisces", 0),
    )
lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Esto se hace en el log para ir probando y ver que funciona, luego ya se quita
        /*for (horoscope in horoscopeList)
            Log.i("HOROSCOPE", horoscope.name)
         */

        recyclerView = findViewById(R.id.recyclerView)

        val adapter = HoroscopeAdapter(horoscopeList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


    }
}