package com.example.mcdonalds.controller

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.fragments.DetailsFragment
import com.example.mcdonalds.model.McItem
import com.example.mcdonalds.model.SingleMcItem
import com.example.mcdonalds.utils.FragmentUtils

class ProductAdapter(private val items : List<McItem>, private val activity : AppCompatActivity) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_menu, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = items[position].getName()

        //Recover Internal Image from name
        val resourceString = "@drawable/${items[position].getImage()}"
        val resourcesId = activity.resources.getIdentifier(resourceString, "drawable", activity.packageName)

        holder.itemImage.setImageResource(resourcesId)
        holder.itemImage.contentDescription = items[position].getImageDesc()
        holder.itemImage.contentDescription = items[position].getImageDesc()

        holder.itemView.setOnClickListener{
            FragmentUtils.changeCurrentFragment(activity, DetailsFragment(items[holder.layoutPosition] as SingleMcItem), items[holder.layoutPosition].getName())
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView = itemView.findViewById(R.id.card_menu_image)
        var itemTitle : TextView = itemView.findViewById(R.id.card_menu_text)
    }
}