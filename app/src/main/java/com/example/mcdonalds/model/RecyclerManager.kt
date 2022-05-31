package com.example.mcdonalds.model

import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView

interface RecyclerManager {

    fun updateRecyclerView(recyclerView: RecyclerView, recyclerAdapter: Adapter)
}