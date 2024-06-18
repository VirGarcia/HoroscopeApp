package com.example.horoscopeapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.horoscopeapp.data.Horoscope
import com.example.horoscopeapp.adapters.HoroscopeAdapter
import com.example.horoscopeapp.data.HoroscopeProvider
import com.example.horoscopeapp.R
import com.example.horoscopeapp.utils.SessionManager


class MainActivity : AppCompatActivity() {

   lateinit var horoscopeList: List<Horoscope>
   lateinit var recyclerView: RecyclerView
   lateinit var adapter: HoroscopeAdapter
   lateinit var userNameText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val session = SessionManager(this)

        if (session.getUserName() == null) {

        } else {

        }

        /*if (!USER_NAME) {

            userNameText = findViewById(R.id.userNameText)


        } else {
            // El campo de texto no está vacío
        }

         */







        horoscopeList = HoroscopeProvider.findAll()
        recyclerView = findViewById(R.id.recyclerView)

        adapter = HoroscopeAdapter(horoscopeList) { position ->
            navigateToDetail(horoscopeList[position])
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
    override fun onResume() {
        super.onResume()
        adapter.updateData(horoscopeList)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)

        val searchViewItem = menu.findItem(R.id.menu_search)
        val searchView = searchViewItem.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    horoscopeList = HoroscopeProvider.findAll().filter {
                        getString(it.name).contains(newText, true) ||
                                getString(it.description).contains(newText, true)
                    }
                    adapter.updateData(horoscopeList, newText)
                }
                return true
            }
        })

        return true
    }

    fun navigateToDetail(horoscope: Horoscope) {
        val intent: Intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("EXTRA_HOROSCOPE_ID", horoscope.id)
        startActivity(intent)
    }
}