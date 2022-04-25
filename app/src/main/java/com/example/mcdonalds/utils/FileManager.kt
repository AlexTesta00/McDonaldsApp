package com.example.mcdonalds.utils

import android.content.Context
import com.example.mcdonalds.model.SingleMcItem

class FileManager {

    companion object{

        private const val PENDING_FILE_NAME : String = "pendingOrder.txt"

        fun writeOnPendingOrderFile(context: Context,mcItem: SingleMcItem){
            TODO()
        }

        fun readFromPendingOrderFile() : List<SingleMcItem>{
            TODO()
        }

        fun clearPendingOrderFile(){
            TODO()
        }
    }
}