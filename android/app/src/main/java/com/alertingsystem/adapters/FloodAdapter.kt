package com.alertingsystem.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alertingsystem.R
import com.alertingsystem.core.Flood
import kotlinx.android.synthetic.main.flood_layout_item.view.*

class FloodAdapter(var data: List<Flood>, private val mContext: Context) : RecyclerView.Adapter<FloodAdapter.FloodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FloodViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.flood_layout_item, parent, false);
        return FloodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FloodViewHolder, position: Int) {
        val flood = data[position]
        holder.itemView.timeTextView.text = "Time: ${flood.originTime}"
        holder.itemView.tipsTextView.text = "Time: ${flood.tips}"
        holder.itemView.speedTextView.text = "Time: ${flood.speed}"
        holder.itemView.heightTextView.text = "Time: ${flood.height}"
        holder.itemView.levelTextView.text = "Time: ${flood.level}"
        holder.itemView.latlonTextView.text = "Location: ${flood.lat} ${flood.lon}"
    }

    inner class FloodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}