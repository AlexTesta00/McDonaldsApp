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
import com.example.mcdonalds.model.Ingredient
import com.example.mcdonalds.model.McItem
import com.example.mcdonalds.model.SingleMcItem
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
        this.productAdapter = ProductAdapter(this.getItems(), activity as AppCompatActivity)
        this.productView.adapter = this.productAdapter
    }

    private fun bindComponents(activity: Activity){
        this.categoryView = activity.findViewById(R.id.rv_categories)
        this.productView = activity.findViewById(R.id.rv_products)
    }

    private fun getItems() : List<McItem>{
        //McChiken
        val mcChikenBread = Ingredient("Bread", 200.00f, 156)
        val mcChikenChiken = Ingredient("Chiken", 200.00f, 200)
        val mcChikenSalad = Ingredient("Salad", 100.00f, 100)
        val mcChikenSauls = Ingredient("McChikenSauls", 0.50f, 45)
        val cheddar = Ingredient("Cheddar", 10.00f, 35)
        val bacon = Ingredient("Bacon", 10.00f, 105)
        val cucumber = Ingredient("Cucumber", 40.00f, 30)
        val onion = Ingredient("Onion", 30.00f, 50)

        val mcChiken : McItem = SingleMcItem("McChiken",
            R.drawable.mcchiken,
            "McChikenDescription",
            4.60,
            true,
            Category("Hamburger"),
            mutableListOf(mcChikenBread, mcChikenChiken, mcChikenSalad, mcChikenSauls)
        )

        //McCrispy
        val mcCrispy : McItem = SingleMcItem("McCrispy",
            R.drawable.crispy,
            "McCrispyDescription",
            5.20,
            true,
            Category("Hamburger"),
            mutableListOf(mcChikenBread, mcChikenChiken, mcChikenSalad, mcChikenSauls, cheddar, bacon)
        )

        //BigMac
        val bigMac : McItem = SingleMcItem("BigMac",
            R.drawable.bigmac,
            "BigMacDesc",
            5.00,
            true,
            Category("Hamburger"),
            mutableListOf(mcChikenBread, mcChikenChiken, mcChikenSalad, mcChikenSauls, cheddar, onion, cucumber)
        )

        //Hamburger
        val hamburger : McItem = SingleMcItem("Hamburger",
            R.drawable.hamburger,
            "Hamburger Description",
            1.50,
            true,
            Category("Hamburger"),
            mutableListOf(mcChikenBread, mcChikenChiken, mcChikenSauls, cheddar, onion, cucumber)
        )

        //FiletFish

        return listOf(mcChiken, mcCrispy, bigMac, hamburger)
    }
}