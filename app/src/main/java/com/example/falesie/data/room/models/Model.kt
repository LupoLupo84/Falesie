package com.example.falesie.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "shopping_list")
data class ShoppingList(
    @ColumnInfo(name = "list_id")
    @PrimaryKey
    val id:Int,
    val name:String
)

@Entity(tableName = "items")
data class Item(
    @ColumnInfo(name = "item_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val itemName:String,
    val qty:String,
    val listId:Int,
    val storeIdFk:Int,
    val date: Date,
    val isChecked:Boolean
    )

@Entity(tableName = "vie")
data class Via(
    @ColumnInfo(name = "via_id")
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val viaName:String,
    val settore:String,
    val numero: Int,
    val falesiaIdFk:String,
    val grado: String,
    val protezioni:Int,
    val altezza: Int,
    val immagine: String,
    val isChecked:Boolean
)

@Entity(tableName = "falesia")
data class Falesia(
    @ColumnInfo(name = "falesia_id")
    @PrimaryKey(autoGenerate = false)
    var id: String ,
    val nome: String ,
    val descrizione: String ,
    var latitudine: String ,
    var longitudine: String ,
    val stagioni: Int ,
    val altitudine: Int ,
    val primavera: Boolean ,
    val estate: Boolean ,
    val autunno: Boolean ,
    val inverno: Boolean ,
)


@Entity(tableName = "stores")
data class Store(
    @ColumnInfo(name = "store_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val listIdFk: Int,
    val storeName: String,
)