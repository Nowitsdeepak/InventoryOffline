package com.example.inventoryoffline.data

import android.app.Application

class ItemApplication : Application() {

    val database: ItemDatabase by lazy { ItemDatabase.getDatabase(this) }

}