package com.example.falesie.model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Falesia(
    var id: String = "",
    val nome: String = "",
    val descrizione: String = "",
    var latitudine: String = "",
    var longitudine: String = "",
    val stagioni: Int = 0,
    val altitudine: Int = 0,
    val primavera: Boolean = false,
    val estate: Boolean = false,
    val autunno: Boolean = false,
    val inverno: Boolean = false,
): Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readBoolean(),
    )

    override fun describeContents(): Int {
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nome)
        parcel.writeString(descrizione)
        parcel.writeString(latitudine)
        parcel.writeString(longitudine)
        parcel.writeInt(stagioni)
        parcel.writeInt(altitudine)
        parcel.writeBoolean(primavera)
        parcel.writeBoolean(estate)
        parcel.writeBoolean(autunno)
        parcel.writeBoolean(inverno)
    }

    companion object CREATOR : Parcelable.Creator<Falesia> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Falesia {
            return Falesia(parcel)
        }

        override fun newArray(size: Int): Array<Falesia?> {
            return arrayOfNulls(size)
        }
    }
}