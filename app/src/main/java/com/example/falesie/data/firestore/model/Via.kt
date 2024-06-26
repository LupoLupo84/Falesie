package com.example.falesie.data.firestore.model

import android.os.Parcel
import android.os.Parcelable

data class Via(
    var id: String = "",
    var falesia: String = "",
    var settore: String = "",
    var numero: Int = 0,
    var nome: String = "",
    var grado: String = "",
    var protezioni: Int = 0,
    var altezza: Int = 0,
    var immagine: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(falesia)
        parcel.writeString(settore)
        parcel.writeInt(numero)
        parcel.writeString(nome)
        parcel.writeString(grado)
        parcel.writeInt(protezioni)
        parcel.writeInt(altezza)
        parcel.writeString(immagine)
    }

    companion object CREATOR : Parcelable.Creator<Via> {
        override fun createFromParcel(parcel: Parcel): Via {
            return Via(parcel)
        }

        override fun newArray(size: Int): Array<Via?> {
            return arrayOfNulls(size)
        }
    }
}
