package com.example.falesie.ui.repository

import android.util.Log
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.transform

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

    //val storeVieFalesia = viaDao.getVieFalesia()


    fun getVieFalesia(falesia:String): Flow<List<Via>>{
        val storeVieFalesia = viaDao.getVieFalesia(falesia)
        return storeVieFalesia
    }

    fun getVieFalesiaSettore(falesia:String, settore:String): Flow<List<Via>>{
        val storeVieFalesia = viaDao.getVieFalesiaSettore(falesia,settore)
        return storeVieFalesia
    }

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