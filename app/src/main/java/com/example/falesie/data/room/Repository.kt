package com.example.falesie.data.room

import com.example.falesie.data.room.models.Falesia
import com.example.falesie.data.room.models.Via

interface ViaRepository {

    suspend fun insert(via: Via)

    suspend fun update(via: Via)

    suspend fun delete(via: Via)

    suspend fun getAllVie(): List<Via>
    fun getVieFalesia(falesia: String): List<Via>

    suspend fun getNomiSettore(falesia_id: String): List<String>

    fun getVia(via_id: String): Via

    suspend fun deleteDatabaseVie()


}


interface FalesiaRepository {

    suspend fun insert(falesia: Falesia)

    suspend fun update(falesia: Falesia)

    suspend fun delete(falesia: Falesia)

    suspend fun getAllFalesieNew(): List<Falesia>

    suspend fun getNomeFalesia(falesia_id: String): String

    suspend fun getNomiFalesie(): List<String>

    suspend fun deleteDatabaseFalesia()


}