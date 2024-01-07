package com.example.falesie.data.firestore.model

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class ViaScalata(
    val id: String = "",
    //var dataRipetizioni: List<String> = emptyList()
    var dataRipetizioni: MutableList<String> = ArrayList()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createStringArrayList()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeStringList(dataRipetizioni)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ViaScalata> {
        override fun createFromParcel(parcel: Parcel): ViaScalata {
            return ViaScalata(parcel)
        }

        override fun newArray(size: Int): Array<ViaScalata?> {
            return arrayOfNulls(size)
        }
    }


}