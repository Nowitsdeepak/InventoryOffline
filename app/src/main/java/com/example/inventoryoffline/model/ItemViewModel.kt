package com.example.inventoryoffline.model

import android.util.Log
import androidx.lifecycle.*
import com.example.inventoryoffline.data.Item
import com.example.inventoryoffline.data.ItemDao
import kotlinx.coroutines.launch
import kotlin.math.abs

const val TAG = "TAG"

enum class Operations(val label_name: String) {
    EDIT_ADD_ITEM("Edit Add Item"),
    APPLY_DISCOUNT("Apply Discount"),
    ADD_QUANTITY("Add Quantity"),
    RETURN_ITEM("Return Item"),
    DELETE_ITEM("Delete")
}

class ItemViewModel(private val itemDao: ItemDao) : ViewModel() {


    fun getAllItems(): LiveData<List<Item>> = itemDao.getItems().asLiveData()

    fun getItemById(id: Long): LiveData<Item> = itemDao.getItem(id).asLiveData()


    fun insert(
        ProductName: String,
        CostPrice: String,
        Quantity: String,
        SellingPrice: String,
    ) {
        val item = insertConverter(
            ProductName = ProductName,
            CostPrice = CostPrice,
            Quantity = Quantity,
            SellingPrice = SellingPrice
        )

        insertItem(item)

    }


    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }


    fun updateHomeItem(item: Item, sprice: Int, squantity: Int) {

        if (item.quantity > 0) {

            val newItem = item.copy(
                quantity = item.quantity - squantity,
                sellingPrice = sprice,
                sold = item.sold + squantity,
                totalSelling = item.totalSelling + (sprice * squantity),
                totalCost = item.totalCost + (squantity * item.costPrice),
                profitLoss = (item.totalSelling.plus(squantity * sprice)).minus((item.totalCost + (squantity * item.costPrice)))
            )

            viewModelScope.launch {
                itemDao.update(newItem)
            }

        }
    }


    fun updateOperation(
        item: Item,
        ProductName: String,
        CostPrice: String,
        Quantity: String,
        SellingPrice: String,
    ) {
        val alpha = SellingPrice.ifBlank { "0" }
        val newItem = item.copy(
            productName = ProductName,
            costPrice = CostPrice.toInt(),
            quantity = Quantity.toInt(),
            sellingPrice = alpha.toInt()
        )
        viewModelScope.launch {
            itemDao.update(newItem)
            Log.d(TAG, "O Updated")
            Log.d(TAG, "$newItem")
        }

    }


    private fun insertConverter(
        ProductName: String,
        CostPrice: String,
        Quantity: String,
        SellingPrice: String,
    ): Item {
        val alpha = SellingPrice.ifBlank { "0" }

        return Item(
            productName = ProductName,
            costPrice = CostPrice.toInt(),
            quantity = Quantity.toInt(),
            sellingPrice = alpha.toInt()

        )
    }

    fun discountOperation(item: Item, discount: Int) {
        val newItem = item.copy(
            totalSelling = item.totalSelling.minus(abs(discount)),
            totalCost = item.totalCost,
            profitLoss = item.totalSelling.minus(abs(discount)).minus(item.totalCost)
        )
        viewModelScope.launch { itemDao.update(newItem) }
    }

    fun returnOperation(item: Item, Cost: Int, Selling: Int, rQuantity: Int) {
        val cost = abs(Cost)
        val selling = abs(Selling)
        val rquantity = abs(rQuantity)

        val newItem = item.copy(
            quantity = item.quantity.plus(rQuantity),
            sold = item.sold.minus(rQuantity),
            totalSelling = item.totalSelling.minus((rquantity * selling)),
            totalCost = item.totalCost.minus((rquantity * cost)),
            profitLoss = (item.totalSelling.minus((rquantity * selling))
                .minus(item.totalCost.minus((rquantity * cost)))
                    )
        )

        viewModelScope.launch { itemDao.update(newItem) }
    }


    fun addQuantityOperation(item: Item, aQuantity: Int) {
        val newItem = item.copy(quantity = item.quantity.plus(abs(aQuantity)))
        viewModelScope.launch { itemDao.update(newItem) }
    }

    fun delete(id: Long) {
        viewModelScope.launch { itemDao.delete(id) }
    }

    fun clearDatabase() {
        viewModelScope.launch { itemDao.clearDatabase() }
    }

    fun validate(
        ProductName: String,
        CostPrice: String,
        Quantity: String,
    ): Boolean {
        return ProductName.isNotBlank() && CostPrice.isNotBlank() && Quantity.isNotBlank()
    }


}


//
class ItemViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return ItemViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}