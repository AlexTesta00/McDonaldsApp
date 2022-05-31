package com.example.mcdonalds.controller

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import java.util.stream.Collectors

class HistoryAdapter(private val oldOrders : Map<String, List<String>>)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_history, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {

        val currentKey = this.oldOrders.keys.stream().collect(Collectors.toList())[position]

        holder.itemId.text = currentKey

        holder.itemDetails.text = this.oldOrders[currentKey].toString()

        Log.d("valore", "Sto settando gli elementi")

        holder.itemView.setOnClickListener {
            Log.d("valore", "Posizione elemento : ${holder.layoutPosition}")
        }
    }

    override fun getItemCount(): Int {
        return this.oldOrders.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var itemId : TextView = itemView.findViewById(R.id.txt_order_id)
        var itemDetails : TextView = itemView.findViewById(R.id.txt_order_details)
    }

}