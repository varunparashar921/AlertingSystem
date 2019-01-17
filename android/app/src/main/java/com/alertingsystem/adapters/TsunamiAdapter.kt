package com.alertingsystem.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alertingsystem.R
import com.alertingsystem.core.Tsunami
import kotlinx.android.synthetic.main.tsunami_layout_item.view.*

class TsunamiAdapter(var data: List<Tsunami>, private val mContext: Context) : RecyclerView.Adapter<TsunamiAdapter.TsunamiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TsunamiViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.tsunami_layout_item, parent, false);
        return TsunamiViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TsunamiViewHolder, position: Int) {
        val tsunami = data[position]
        holder.itemView.timeTextView.text = "Time: ${tsunami.originTime}"
        holder.itemView.magnitudeTextView.text = "Magnitude: ${tsunami.magnitude}"
        holder.itemView.depthTextView.text = "Height: ${tsunami.height}"
        holder.itemView.speedTextView.text = "Height: ${tsunami.speed}"
        holder.itemView.epicCenterTextView.text = "Height: ${tsunami.epicenter}"
        holder.itemView.latlonTextView.text = "Location: ${tsunami.lat} ${tsunami.lon}"
    }

    inner class TsunamiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}