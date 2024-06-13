package com.example.horoscopeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class MainActivity : AppCompatActivity() {

   lateinit var horoscopeList: List<Horoscope>
   lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        horoscopeList = HoroscopeProvider.findAll()

        //Esto se hace en el log para ir probando y ver que funciona, luego ya se quita
        /*for (horoscope in horoscopeList)
            Log.i("HOROSCOPE", horoscope.name)
         */

        recyclerView = findViewById(R.id.recyclerView)

        val adapter = HoroscopeAdapter(horoscopeList) { position ->
            navigateToDetail(horoscopeList[position])
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }
    fun navigateToDetail(horoscope: Horoscope) {
        val intent: Intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("EXTRA_HOROSCOPE_ID", horoscope.id)

        startActivity(intent)

    }
}