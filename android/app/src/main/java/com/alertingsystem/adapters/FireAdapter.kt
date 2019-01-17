package com.alertingsystem.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alertingsystem.R
import com.alertingsystem.core.Fire
import kotlinx.android.synthetic.main.fire_layout_item.view.*

class FireAdapter(var data: List<Fire>, private val mContext: Context) : RecyclerView.Adapter<FireAdapter.FireViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FireViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.fire_layout_item, parent, false);
        return FireViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FireViewHolder, position: Int) {
        val fire = data[position]
        holder.itemView.timeTextView.text = "Time: ${fire.originTime}"
        holder.itemView.brightnessTextView.text = "Temperature: ${fire.temperature}"
        holder.itemView.satelliteTextView.visibility = View.GONE
        holder.itemView.latlonTextView.text = "Location: ${fire.lat} ${fire.lon}"
    }

    inner class FireViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}