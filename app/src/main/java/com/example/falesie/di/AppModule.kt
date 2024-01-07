package com.example.falesie.di

import android.app.Application
import androidx.room.Room
import com.example.falesie.data.room.FalesiaRepository
import com.example.falesie.data.room.FalesiaRepositoryImpl
import com.example.falesie.data.room.FalesieDatabase
import com.example.falesie.data.room.ViaRepository
import com.example.falesie.data.room.ViaRepositoryImpl
import com.example.falesie.data.room.VieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //*********************** DATABASE VIA *****************

    @Provides
    @Singleton
    fun provideViaDatabase(app: Application) : VieDatabase {
        return Room.databaseBuilder(
            app,
            VieDatabase::class.java,
            "vie_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideViaRepository(db : VieDatabase) : ViaRepository {
        return ViaRepositoryImpl(db.viaDAO)
    }


    //*********************** DATABASE FALESIA *****************

    @Provides
    @Singleton
    fun provideFalesiaDatabase(app: Application) : FalesieDatabase {
        return Room.databaseBuilder(
            app,
            FalesieDatabase::class.java,
            "falesie_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFalesiaRepository(db : FalesieDatabase) : FalesiaRepository {
        return FalesiaRepositoryImpl(db.falesiaDAO)
    }

}