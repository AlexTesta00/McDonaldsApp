package com.example.mcdonalds.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.controller.HistoryAdapter
import com.example.mcdonalds.model.McOrder
import com.example.mcdonalds.utils.FragmentUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HistoryFragment : Fragment() {

    private lateinit var recycler : RecyclerView
    private lateinit var adapter : HistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity != null){
            this.bindComponents(activity as AppCompatActivity)
            this.getHistory()
            FragmentUtils.changeAppBarName(activity as AppCompatActivity, getString(R.string.history))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    private fun bindComponents(activity : Activity){
        this.recycler = activity.findViewById(R.id.history_recycler)
    }

    private fun setRecyclerAdapter(oldOrders : Map<String, List<String>>){
        Log.d("valore", "Setto la recycler view")
        Log.d("valore", "Recycler Items : $oldOrders")
        this.adapter = HistoryAdapter(oldOrders, activity as AppCompatActivity)
        this.recycler.adapter = this.adapter
    }

    private fun getHistory(){
        val database = Firebase.database
        val reference = database.getReference(McOrder.getUserInfo()!!.email.split("@")[0])


        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null){
                    val value = snapshot.value as Map<String, List<String>>

                    //Set the item on recycler view
                    if (activity != null){
                        setRecyclerAdapter(value)
                    }

                }else{
                    //Todo implement no order
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onResume() {
        super.onResume()

        if(activity != null){
            this.bindComponents(activity as AppCompatActivity)
            this.getHistory()
            FragmentUtils.changeAppBarName(activity as AppCompatActivity, getString(R.string.history))
        }
    }
}