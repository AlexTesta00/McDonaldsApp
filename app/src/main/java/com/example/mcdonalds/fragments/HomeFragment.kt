package com.example.mcdonalds.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.controller.CategoryAdapter
import com.example.mcdonalds.model.Category
import com.example.mcdonalds.utils.FragmentUtils
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var categoryView : RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity != null){
            bindComponents(activity as AppCompatActivity)
            setCategoryRecyclerView()
            FragmentUtils.changeAppBarName(activity as AppCompatActivity, getString(R.string.home))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun setCategoryRecyclerView(){
        categoryView.setHasFixedSize(true)
        val mutableCategoryList : MutableList<Category> = mutableListOf()
        mutableCategoryList.add(Category("Hamburger"))
        mutableCategoryList.add(Category("Drink"))
        mutableCategoryList.add(Category("Snack"))
        mutableCategoryList.add(Category("Dessert"))

        this.categoryAdapter = CategoryAdapter(mutableCategoryList)
        this.categoryView.adapter = categoryAdapter
    }

    private fun bindComponents(activity: Activity){
        this.categoryView = activity.findViewById(R.id.rv_categories)
    }
}