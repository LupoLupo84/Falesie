package com.example.falesie.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.falesie.data.room.models.Via

@Database(
    entities = [Via::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseVie: RoomDatabase() {
    abstract fun vieDao(): ViaDao

    companion object{
        @Volatile
        var INSTANCE: DatabaseVie? = null
        fun getDatabase(context: Context):DatabaseVie{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    DatabaseVie::class.java,
                    "database_vie_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}


