package com.example.mcdonalds.utils


import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mcdonalds.R

class FragmentUtils {

    companion object{
        fun changeCurrentFragment(activity: AppCompatActivity, fragment: Fragment, tag: String) : Unit {
            if(fragment != null){
                val transaction:FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment, tag)
                transaction.commit()

                //Change AppBar Title
                //setTitleOfToolBar(activity, tag)
            }
        }

        private fun setTitleOfToolBar(activity: AppCompatActivity, title: String) : Unit{
            activity.findViewById<TextView>(R.id.app_bar_title).text = title
        }
    }
}