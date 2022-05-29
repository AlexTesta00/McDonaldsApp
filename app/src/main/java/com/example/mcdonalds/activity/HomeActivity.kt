package com.example.mcdonalds.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.mcdonalds.R
import com.example.mcdonalds.fragments.CartFragment
import com.example.mcdonalds.fragments.HomeFragment
import com.example.mcdonalds.fragments.ScanFragment
import com.example.mcdonalds.model.McOrder
import com.example.mcdonalds.model.McUser
import com.example.mcdonalds.utils.FragmentUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import nl.joery.animatedbottombar.AnimatedBottomBar

class HomeActivity : AppCompatActivity(){

    //Components
    private lateinit var bottomNavigationView : AnimatedBottomBar
    private lateinit var firebaseAuth: FirebaseAuth
    private val homeFragment : HomeFragment = HomeFragment()
    private val cartFragment : CartFragment = CartFragment()
    private val scanFragment : ScanFragment = ScanFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Check if User are auth
        this.firebaseAuth = FirebaseAuth.getInstance()
        this.checkUser()

        //Attach view components
        this.attachAllComponents()

        //Activate All Listener
        this.setAllListener(this@HomeActivity)

        //Set Id
        McOrder.changeId()

        //Set the custom AppBar
        this.setCustomAppBar()

        //Set the first view
        FragmentUtils.changeCurrentFragment(this, homeFragment, resources.getString(R.string.home))

    }

    private fun checkUser(){
        val firebaseUser : FirebaseUser? = firebaseAuth.currentUser
        if(firebaseUser == null){
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finish()
        }else{
            McOrder.setUser(McUser(firebaseUser.email!!, firebaseUser.uid))
        }
    }

    private fun attachAllComponents(){
        this.bottomNavigationView = findViewById(R.id.bottom_navigation_menu)
    }

    private fun setAllListener(activity: AppCompatActivity){
        //Bottom NavigationBar Listener
        this.bottomNavigationView.onTabSelected = {
            when(it.id){
                R.id.menu_home -> FragmentUtils.changeCurrentFragment(activity,homeFragment, resources.getString(
                    R.string.home
                ))
                R.id.menu_cart -> FragmentUtils.changeCurrentFragment(activity,cartFragment, resources.getString(
                    R.string.cart
                ))
                R.id.menu_qr_code -> FragmentUtils.changeCurrentFragment(activity,scanFragment, resources.getString(
                    R.string.history
                ))
            }
        }
    }

    private fun setCustomAppBar(){
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_app_bar)
    }
}