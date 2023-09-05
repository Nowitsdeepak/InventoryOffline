package com.example.inventoryoffline.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Query("DELETE FROM item WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM item")
    suspend fun clearDatabase()

    @Query("SELECT * FROM item")
    fun getItems(): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE id =:id")
    fun getItem(id: Long): Flow<Item>

}