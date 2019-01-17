package com.alertingsystem.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alertingsystem.R
import com.alertingsystem.core.EarthQuake
import kotlinx.android.synthetic.main.earthquake_layout_item.view.*

class EarthQuakeAdapter(var data: List<EarthQuake>, private val mContext: Context) : RecyclerView.Adapter<EarthQuakeAdapter.EarthQuakeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EarthQuakeViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.earthquake_layout_item, parent, false);
        return EarthQuakeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: EarthQuakeViewHolder, position: Int) {
        val tsunami = data[position]
        holder.itemView.timeTextView.text = "Time: ${tsunami.originTime}"
        holder.itemView.magnitudeTextView.text = "Magnitude: ${tsunami.magnitude}"
        holder.itemView.latlonTextView.text = "Location: ${tsunami.lat} ${tsunami.lon}"
    }

    inner class EarthQuakeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
