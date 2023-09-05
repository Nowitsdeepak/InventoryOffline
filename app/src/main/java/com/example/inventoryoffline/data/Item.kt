package com.example.inventoryoffline.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "product_name")
    val productName: String,
    @ColumnInfo(name = "cost")
    val costPrice: Int,
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    @ColumnInfo(name = "selling_price")
    val sellingPrice: Int = 0,
    @ColumnInfo(name = "sold")
    val sold: Int = 0,
    @ColumnInfo(name = "total_selling")
    val totalSelling: Int = 0,
    @ColumnInfo(name = "total_cost")
    val totalCost: Int = 0,
    @ColumnInfo(name = "profit_loss")
    val profitLoss: Int = 0,
)
