package com.example.falesie.ui.repository

import com.example.falesie.data.room.FalesiaDao
import com.example.falesie.data.room.ItemDao
import com.example.falesie.data.room.ListDao
import com.example.falesie.data.room.StoreDao
import com.example.falesie.data.room.ViaDao
import com.example.falesie.data.room.models.Falesia
import com.example.falesie.data.room.models.Item
import com.example.falesie.data.room.models.ShoppingList
import com.example.falesie.data.room.models.Store
import com.example.falesie.data.room.models.Via

class Repository(
    private val listDao: ListDao,
    private val storeDao: StoreDao,
    private val itemDao: ItemDao,
    private val viaDao: ViaDao,
    private val falesiaDao: FalesiaDao
) {

    val store = storeDao.getAllStores()
    val getItemsWithListAndStore = listDao.getItemsWithStoreAndList()

    fun getItemWithStoreAndList(id:Int) = listDao.getItemWithStoreAndListFilteredById(id)

    fun getItemWithStoreAndListFilteredById(id:Int) = listDao.getItemsWithStoreAndListFilteredById(id)

    suspend fun insertList(shoppingList: ShoppingList){
        listDao.insertShoppingList(shoppingList)
    }

    suspend fun insertStore(store: Store){
        storeDao.insert(store)
    }

    suspend fun insertItem(item: Item){
        itemDao.insert(item)
    }

    suspend fun deleteItem(item: Item){
        itemDao.delete(item)
    }

    suspend fun updateItem(item: Item){
        itemDao.update(item)
    }



    val storeVie = viaDao.getAllVie()
    suspend fun insertVia(via: Via){
        viaDao.insert(via)
    }

    suspend fun deleteVia(via: Via){
        viaDao.delete(via)
    }

    suspend fun updateVia(via: Via){
        viaDao.update(via)
    }

    val storeFalesie = falesiaDao.getAllFalesie()
    suspend fun insertFalesia(falesia: Falesia){
        falesiaDao.insert(falesia)
    }


}