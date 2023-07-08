package com.example.falesie.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val firstName: String,
    val lastName: String,
    val phoneNumber: String

)

@Entity
data class Viar(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",

    var falesia: String = "",
    val settore: String = "",
    val numero: Int = 0,
    var nome: String = "",
    val grado: String = "",
    val protezioni: Int = 0,
    val altezza: Int = 0,
    val immagine: String = ""

)