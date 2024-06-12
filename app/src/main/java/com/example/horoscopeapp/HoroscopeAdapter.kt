package com.example.horoscopeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class HoroscopeAdapter(private val dataSet: List<Horoscope>, private val onItemClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<HoroscopeViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoroscopeViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horoscope, parent, false)


        return HoroscopeViewHolder(view)
    }

    // Este método simplemente es para decir cuantos elementos queremos mostrar
    override fun getItemCount(): Int {
        return dataSet.size
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: HoroscopeViewHolder, position: Int) {
        val horoscope = dataSet[position]
        //holder.textView.text = horoscope.name
        holder.render(horoscope)
        holder.itemView.setOnClickListener {
            onItemClickListener(position)
        }
    }

}
class HoroscopeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val nameTextView: TextView
    val descTextView: TextView
    val logoImageView: ImageView


    init {
        // Define click listener for the ViewHolder's View

        nameTextView = view.findViewById(R.id.nameTextView)
        descTextView = view.findViewById(R.id.descTextView)
        logoImageView = view.findViewById(R.id.logoImageView)
    }
    fun render(horoscope: Horoscope) {
        nameTextView.setText(horoscope.name)
        descTextView.setText(horoscope.description)
        logoImageView.setImageResource(horoscope.logo)
    }
}
