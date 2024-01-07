package com.example.falesie.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.falesie.data.room.models.Falesia
import com.example.falesie.data.room.models.Via

@Database(entities = [Via::class], version = 1)
abstract class VieDatabase : RoomDatabase() {
    abstract val viaDAO : ViaDao
}

@Database(entities = [Falesia::class], version = 1)
abstract class FalesieDatabase : RoomDatabase() {
    abstract val falesiaDAO : FalesiaDao
}