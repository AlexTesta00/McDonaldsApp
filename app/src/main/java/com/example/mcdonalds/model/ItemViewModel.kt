package com.example.mcdonalds.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ItemViewModel : ViewModel() {
    private val mutableSelectedItem = MutableLiveData<McItem>()
    val selectedItem: LiveData<McItem> get() = mutableSelectedItem

    fun selectItem(item: McItem) {
        mutableSelectedItem.value = item
    }

}