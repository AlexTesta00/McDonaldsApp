package com.example.mcdonalds.controller

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.model.Category

class CategoryAdapter(private val categoryList : List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedItemPosition : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.elevated_button, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        //Set the name of category at button
        val itemList = categoryList[position]
        holder.buttonCategory.text = itemList.name

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    class CategoryViewHolder(category: View) : RecyclerView.ViewHolder(category){
        val buttonCategory : Button = category.findViewById(R.id.btn_category)
    }
}