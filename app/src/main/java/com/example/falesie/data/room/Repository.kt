package com.example.falesie.data.room

import com.example.falesie.data.room.models.Falesia
import com.example.falesie.data.room.models.Via
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.Flow

interface ViaRepository {

    suspend fun insert(via: Via)

    suspend fun update(via: Via)

    suspend fun delete(via: Via)

    fun getAllVie(): Flow<List<Via>>

    fun getVia(via_id:Int): Flow<Via>

    fun getVieFalesia(falesia:String): Flow<List<Via>>

    fun getVieFalesiaSettore(falesia:String,settore:String): Flow<List<Via>>
}


interface FalesiaRepository{

    suspend fun insert(falesia: Falesia)

    suspend fun update(falesia: Falesia)

    suspend fun delete(falesia: Falesia)

    fun getAllFalesie(): Flow<List<Falesia>>

    fun getFalesia(falesia_id:Int): Flow<Falesia>

}