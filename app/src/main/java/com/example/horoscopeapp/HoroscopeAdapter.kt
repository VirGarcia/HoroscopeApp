package com.example.horoscopeapp

import android.graphics.Color
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class HoroscopeAdapter(private var dataSet: List<Horoscope>, private val onItemClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<HoroscopeViewHolder>() {
    private var highlightText: String? = null

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
        holder.render(horoscope)
        if (highlightText != null) {
            holder.highlight(highlightText!!)
        }
        holder.itemView.setOnClickListener {
            onItemClickListener(position)
        }

    }
    // Este método sirve para actualizar los datos
    fun updateData (newDataSet: List<Horoscope>) {
        updateData(newDataSet, null)
    }

    // Este método sirve para actualizar los datos
    fun updateData(newDataSet: List<Horoscope>, highlight: String?) {
        this.highlightText = highlight
        dataSet = newDataSet
        notifyDataSetChanged()
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
    fun highlight(text: String) {
        try {
            val highlighted = nameTextView.text.toString().highlight(text)
            nameTextView.text = highlighted
        } catch (e: Exception) { }
        try {
            val highlighted = descTextView.text.toString().highlight(text)
            descTextView.text = highlighted
        } catch (e: Exception) { }
    }
}
fun String.highlight(text: String) : SpannableString {
    val str = SpannableString(this)
    val startIndex = str.indexOf(text, 0, true)
    str.setSpan(BackgroundColorSpan(Color.CYAN), startIndex, startIndex + text.length, 0)
    return str
}
