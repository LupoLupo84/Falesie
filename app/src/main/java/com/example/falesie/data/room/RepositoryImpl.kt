package com.example.falesie.data.room

import com.example.falesie.data.room.models.Falesia
import com.example.falesie.data.room.models.Via


//******************************************* VIA ****************************************************
class ViaRepositoryImpl(private val dao: ViaDao) : ViaRepository {
    override suspend fun insert(via: Via) {
        dao.insert(via)
    }

    override suspend fun update(via: Via) {
        dao.update(via)
    }

    override suspend fun delete(via: Via) {
        dao.delete(via)
    }

    override suspend fun getAllVie(): List<Via> {
        return dao.getAllVieNew()
    }

    override fun getVieFalesia(falesia: String): List<Via> {
        return dao.getVieFalesia(falesia)
    }

    override suspend fun getNomiSettore(falesia_id: String): List<String> {
        return dao.getNomiSettore(falesia_id)
    }

    override fun getVia(via_id: String): Via {
        return dao.getViaMod(via_id)
    }

    override suspend fun deleteDatabaseVie() {
        return dao.deleteDatabaseVie()
    }

    override suspend fun getIdViaFalesiaSettoreNumero(falesiaId: String, settore: String, numero: Int): Via {
        return dao.getIdViaFalesiaSettoreNumero(falesiaId,settore,numero)
    }

}


//***************************************** FALESIA **************************************************

class FalesiaRepositoryImpl(private val dao: FalesiaDao) : FalesiaRepository {
    override suspend fun insert(falesia: Falesia) {
        dao.insert(falesia)
    }

    override suspend fun update(falesia: Falesia) {
        dao.update(falesia)
    }

    override suspend fun delete(falesia: Falesia) {
        dao.delete(falesia)
    }

    override suspend fun getNomeFalesia(falesia_id: String): String {
        return dao.getNomeFalesia(falesia_id)
    }


    override suspend fun getAllFalesieNew(): List<Falesia> {
        return dao.getAllFalesieNew()
    }

    override suspend fun getNomiFalesie(): List<String> {
        return dao.getNomiFalesie()
    }

    override suspend fun deleteDatabaseFalesia() {
        return dao.deleteDatabaseFalesia()
    }


}