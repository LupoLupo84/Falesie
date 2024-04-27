package com.example.falesie.data.room

import androidx.lifecycle.LiveData
import com.example.falesie.data.room.models.Falesia
import com.example.falesie.data.room.models.Via
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.Flow

interface ViaRepository {

    suspend fun insert(via: Via)

    suspend fun update(via: Via)

    suspend fun delete(via: Via)

    suspend fun getAllVie(): List<Via>
    fun getVieFalesia(falesia:String): List<Via>

//    fun getVieFalesiaSettore(falesia:String,settore:String): Flow<List<Via>>

    suspend fun getNomiSettore(falesia_id:String): List<String>



    fun getVia(via_id:String): Via


}


interface FalesiaRepository{

    suspend fun insert(falesia: Falesia)

    suspend fun update(falesia: Falesia)

    suspend fun delete(falesia: Falesia)

    suspend fun getAllFalesieNew(): List<Falesia>

    suspend fun getNomeFalesia(falesia_id:String): String

    suspend fun getNomiFalesie(): List<String>





}