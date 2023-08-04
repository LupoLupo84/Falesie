package com.example.falesie

import android.content.Context
import com.example.falesie.data.room.DatabaseFalesie
import com.example.falesie.data.room.DatabaseVie
import com.example.falesie.data.room.ShoppingListDatabase
import com.example.falesie.ui.repository.Repository

object Graph {
    lateinit var dbvie:DatabaseVie
        private set

    lateinit var dbfalesie: DatabaseFalesie
        private set

    lateinit var db:ShoppingListDatabase
        private set

    val repository by lazy {
        Repository(
            listDao = db.listDao(),
            storeDao = db.storeDao(),
            itemDao = db.itemDao(),
            viaDao = dbvie.vieDao(),
            falesiaDao = dbfalesie.falesiaDao()
        )

    }

    fun provide(context: Context){
        db = ShoppingListDatabase.getDatabase(context)
        dbvie = DatabaseVie.getDatabase(context)
        dbfalesie = DatabaseFalesie.getDatabase(context)
    }

    fun provideVie(context: Context){
        dbvie = DatabaseVie.getDatabase(context)
    }

    fun provideFalesie(context: Context){
        dbfalesie = DatabaseFalesie.getDatabase(context)
    }




}