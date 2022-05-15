package com.example.mcdonalds.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.mcdonalds.R
import com.example.mcdonalds.fragments.CartFragment
import com.example.mcdonalds.fragments.HomeFragment
import com.example.mcdonalds.fragments.ScanFragment
import com.example.mcdonalds.utils.FragmentUtils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeActivity : AppCompatActivity(){

    //Components
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var maps : GoogleMap
    private val homeFragment : HomeFragment = HomeFragment()
    private val cartFragment : CartFragment = CartFragment()
    private val scanFragment : ScanFragment = ScanFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Check if User are auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //Attach view components
        attachAllComponents()

        //Activate All Listener
        setAllListener()

        //Set the first view
        FragmentUtils.changeCurrentFragment(this, homeFragment, resources.getString(R.string.home))

        //Set the selected item menu
        bottomNavigationView.selectedItemId = R.id.menu_home

        //Set Custom AppBar
        setCustomAppBar()

        //Set Custom Icon in BottomNavBar
        bottomNavigationView.itemIconTintList = null
    }

    private fun checkUser(){
        val firebaseUser : FirebaseUser? = firebaseAuth.currentUser
        if(firebaseUser == null){
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finish()
        }else{
            //Set current user
        }
    }

    private fun attachAllComponents(){
        bottomNavigationView = findViewById(R.id.bottom_navigation_menu)
    }

    private fun setAllListener(){
        //Bottom NavigationBar Listener
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_home -> FragmentUtils.changeCurrentFragment(this,homeFragment, resources.getString(
                    R.string.home
                ))
                R.id.menu_cart -> FragmentUtils.changeCurrentFragment(this,cartFragment, resources.getString(
                    R.string.carrello
                ))
                R.id.menu_qr_code -> FragmentUtils.changeCurrentFragment(this,scanFragment, resources.getString(
                    R.string.storico
                ))
            }
            true
        }
    }

    private fun setCustomAppBar(){
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_app_bar)
    }
}