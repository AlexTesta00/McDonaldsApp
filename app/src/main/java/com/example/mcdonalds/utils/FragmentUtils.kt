package com.example.mcdonalds.utils


import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mcdonalds.R

class FragmentUtils {

    companion object{
        fun changeCurrentFragment(activity: AppCompatActivity, fragment: Fragment, tag: String){
            val transaction:FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment, tag)
            transaction.commit()
        }

        fun changeAppBarName(activity: Activity, title: String){
            activity.findViewById<AppCompatTextView>(R.id.app_bar_title).text = title
        }

    }
}