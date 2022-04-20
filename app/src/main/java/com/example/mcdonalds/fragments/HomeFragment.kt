package com.example.mcdonalds.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.controller.CategoryAdapter
import com.example.mcdonalds.controller.ProductAdapter
import com.example.mcdonalds.model.Category
import com.example.mcdonalds.utils.FragmentUtils


class HomeFragment : Fragment() {

    private lateinit var productView: RecyclerView
    private lateinit var categoryView : RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity != null){
            bindComponents(activity as AppCompatActivity)
            setCategoryRecyclerView()
            setProductRecyclerView()
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

    private fun setProductRecyclerView(){
        this.productAdapter = ProductAdapter(
            arrayListOf("McChicken","McCrispy","Hamburger","BigMac", "McChicken","McCrispy","Hamburger","BigMac", "McChicken","McCrispy","Hamburger","BigMac", "McChicken","McCrispy","Hamburger","BigMac"),
            arrayListOf(R.drawable.mcchiken, R.drawable.crispy, R.drawable.hamburger, R.drawable.bigmac, R.drawable.mcchiken, R.drawable.crispy, R.drawable.hamburger, R.drawable.bigmac, R.drawable.mcchiken, R.drawable.crispy, R.drawable.hamburger, R.drawable.bigmac, R.drawable.mcchiken, R.drawable.crispy, R.drawable.hamburger, R.drawable.bigmac))
        this.productView.adapter = this.productAdapter
    }

    private fun bindComponents(activity: Activity){
        this.categoryView = activity.findViewById(R.id.rv_categories)
        this.productView = activity.findViewById(R.id.rv_products)
    }
}