package com.example.falesie.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


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