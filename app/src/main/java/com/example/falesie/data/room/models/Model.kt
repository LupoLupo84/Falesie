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
    var viaName:String,
    var settore:String,
    var numero: Int,
    var falesiaIdFk:String,
    var grado: String,
    var protezioni:Int,
    var altezza: Int,
    var immagine: String,
    val isChecked:Boolean
)

@Entity(tableName = "falesia")
data class Falesia(
    @ColumnInfo(name = "falesia_id")
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var nome: String,
    var descrizione: String,
    var latitudine: String,
    var longitudine: String,
    val stagioni: Int,
    var altitudine: Int,
    var primavera: Boolean,
    var estate: Boolean,
    var autunno: Boolean,
    var inverno: Boolean,
)