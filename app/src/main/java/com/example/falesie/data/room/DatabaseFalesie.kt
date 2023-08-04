package com.example.falesie.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.falesie.data.room.models.Falesia

@Database(
    entities = [Falesia::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseFalesie: RoomDatabase() {
    abstract fun falesiaDao(): FalesiaDao

    companion object{
        @Volatile
        var INSTANCE: DatabaseFalesie? = null
        fun getDatabase(context: Context):DatabaseFalesie{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    DatabaseFalesie::class.java,
                    "database_falesie_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}