package com.example.falesie.room

data class ContactState(
    val contacts: List<Contact> = emptyList(),
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val isAddingContact: Boolean = false,
    val sortType: SortType = SortType.FIRST_NAME
)


data class ViarState(
    val id: String = "",
    val viar: List<Viar> = emptyList(),
    val falesia: String = "",
    val settore: String = "",
    val numero: Int = 0,
    val nome: String = "",
    val grado: String = "",
    val protezioni: Int = 0,
    val altezza: Int = 0,
    val immagine: String = "",
    val isAddingContact: Boolean = false,

)
