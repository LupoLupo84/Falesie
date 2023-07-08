package com.example.falesie.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Contact::class],
    version = 1
)
abstract class ContactDatabase: RoomDatabase() {

    abstract val dao: ContactDao
}


@Database(
    entities = [Viar::class],
    version = 1
)
abstract class ViarDatabase: RoomDatabase() {

    abstract val dao: ViarDao
}