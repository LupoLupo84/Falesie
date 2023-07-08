package com.example.falesie.room

sealed interface ContactEvent {
    object SaveContact: ContactEvent
    data class SetFirstName(val firstName: String): ContactEvent
    data class SetLastName(val lastName: String): ContactEvent
    data class SetPhoneNumber(val phoneNumber: String): ContactEvent
    object ShowDialog: ContactEvent
    object HideDialog: ContactEvent
    data class SortContacts(val SortType: SortType): ContactEvent
    data class DeleteContact(val contact: Contact): ContactEvent
}

sealed interface ViarEvent {
    object SaveViar: ViarEvent
    data class SetId(val id: String): ViarEvent
    data class SetFalesia(val falesia: String): ViarEvent
    data class SetSettore(val settore: String): ViarEvent
    data class SetNumero(val numero: Int): ViarEvent
    data class SetNome(val nome: String): ViarEvent
    data class SetGrado(val grado: String): ViarEvent
    data class SetProtezioni(val protezioni: Int): ViarEvent
    data class SetAltezza(val altezza: Int): ViarEvent
    data class SetImmagine(val immagine: String): ViarEvent
    data class DeleteViar(val contact: Viar): ViarEvent
}
