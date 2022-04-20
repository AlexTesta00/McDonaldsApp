package com.example.mcdonalds.controller

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.model.Category
import com.example.mcdonalds.utils.Constants

class CategoryAdapter(private val categoryList : List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedItemPosition : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.elevated_button, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        //Set the name of category at button
        val itemList = categoryList[position]
        holder.buttonCategory.text = itemList.name

        //Listener for all item in recyclerview
        holder.itemView.setOnClickListener {
            selectedItemPosition = holder.adapterPosition
            //If the item on recycler change
            // the recycler refresh
            this.notifyDataSetChanged()
        }

        //Change the color of button selected
        if(selectedItemPosition == position){
            holder.buttonCategory.setTextColor(Color.parseColor(Constants.WHITE))
            holder.buttonCategory.setBackgroundColor(Color.parseColor(Constants.GREEN))
        }else{
            holder.buttonCategory.setTextColor(Color.parseColor(Constants.BLACK))
            holder.buttonCategory.setBackgroundColor(Color.parseColor(Constants.WHITE))
        }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    class CategoryViewHolder(category: View) : RecyclerView.ViewHolder(category){
        val buttonCategory : Button = category.findViewById(R.id.btn_category)
    }
}