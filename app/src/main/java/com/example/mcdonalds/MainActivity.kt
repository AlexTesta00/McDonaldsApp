package com.example.mcdonalds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mcdonalds.fragments.CartFragment
import com.example.mcdonalds.fragments.HomeFragment
import com.example.mcdonalds.fragments.ScanFragment
import com.example.mcdonalds.utils.FragmentUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    //Components
    private lateinit var bottomNavigationView : BottomNavigationView;
    private val homeFragment : HomeFragment = HomeFragment()
    private val cartFragment : CartFragment = CartFragment()
    private val scanFragment : ScanFragment = ScanFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Attach view components
        attachAllComponents()

        //Activate All Listener
        setAllListener()

        //Set the first view
        FragmentUtils.changeCurrentFragment(this, homeFragment, resources.getString(R.string.home))

    }

    private fun attachAllComponents(){
        bottomNavigationView = findViewById(R.id.bottom_navigation_menu)
    }

    private fun setAllListener(){
        //Bottom NavigationBar Listener
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_home -> FragmentUtils.changeCurrentFragment(this,homeFragment, resources.getString(R.string.home))
                R.id.menu_cart -> FragmentUtils.changeCurrentFragment(this,cartFragment, resources.getString(R.string.carrello))
                R.id.menu_qr_code -> FragmentUtils.changeCurrentFragment(this,scanFragment, resources.getString(R.string.scansiona))
            }
            true
        }
    }
}