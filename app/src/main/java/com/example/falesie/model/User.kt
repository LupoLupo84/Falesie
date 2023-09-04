package com.example.falesie.model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import java.util.*



data class User(
    val id: String = "",
    val nome: String = "",
    val email: String = "",
    val immagine: String = "",
    val telefono: Long = 0,
    val gradoMax: String = "",
    val vieScalate: List<ViaScalata> = emptyList(),
    //val vieScalate: List<ViaScalata> = emptyList(),
    val admin: Boolean = false,
    val test: Timestamp = Timestamp(0,0),
    val aggiora: Boolean = true,
    //val vieScalate: Array<String> = emptyArray()

) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!,
        parcel.createStringArray()!! as List<ViaScalata>,
        parcel.readBoolean(),
        parcel.readParcelable(Timestamp::class.java.classLoader)!!,
        parcel.readBoolean(),
        //parcel.createStringArray() as Array<String>
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nome)
        parcel.writeString(email)
        parcel.writeString(immagine)
        parcel.writeLong(telefono)
        parcel.writeString(gradoMax)
        parcel.writeList(vieScalate)
        parcel.writeBoolean(admin)
        parcel.writeParcelable(test, flags)
        parcel.writeBoolean(aggiora)
        //parcel.writeArray(vieScalate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
