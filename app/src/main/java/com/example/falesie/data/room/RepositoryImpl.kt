package com.example.falesie.data.room

import com.example.falesie.data.room.models.Falesia
import com.example.falesie.data.room.models.Via
import kotlinx.coroutines.flow.Flow



//******************************************* VIA ****************************************************
class ViaRepositoryImpl(private val dao : ViaDao) : ViaRepository {
    override suspend fun insert(via: Via) {
        dao.insert(via)
    }

    override suspend fun update(via: Via) {
        dao.update(via)
    }

    override suspend fun delete(via: Via) {
        dao.delete(via)
    }

    override fun getAllVie(): Flow<List<Via>> {
        return dao.getAllVie()
    }

    override fun getVia(via_id: Int): Flow<Via> {
        return dao.getVia(via_id)
    }

    override fun getVieFalesia(falesia: String): Flow<List<Via>> {
        return dao.getVieFalesia(falesia)
    }

    override fun getVieFalesiaSettore(falesia: String, settore: String): Flow<List<Via>> {
        return dao.getVieFalesiaSettore(falesia, settore)
    }


}


//***************************************** FALESIA **************************************************

class FalesiaRepositoryImpl(private val dao : FalesiaDao) : FalesiaRepository {
    override suspend fun insert(falesia: Falesia) {
        dao.insert(falesia)
    }

    override suspend fun update(falesia: Falesia) {
        dao.update(falesia)
    }

    override suspend fun delete(falesia: Falesia) {
        dao.delete(falesia)
    }

    override fun getAllFalesie(): Flow<List<Falesia>> {
        return dao.getAllFalesie()
    }

    override fun getFalesia(falesia_id: Int): Flow<Falesia> {
        return dao.getFalesia(falesia_id)
    }

}