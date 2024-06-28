package com.example.falesie.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.falesie.data.room.models.Falesia
import com.example.falesie.data.room.models.Via
import kotlinx.coroutines.flow.Flow


@Dao
interface ViaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(via: Via)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(via: Via)

    @Delete
    suspend fun delete(via: Via)

    @Query("SELECT * FROM vie")
    fun getAllVieNew(): List<Via>

    @Query("SELECT * FROM vie WHERE falesiaIdFk =:falesia ORDER BY numero")
    fun getVieFalesia(falesia: String): List<Via>


    @Query("SELECT settore FROM vie WHERE falesiaIdFk =:falesia_id")
    fun getNomiSettore(falesia_id: String): List<String>

    @Query("SELECT * FROM vie WHERE via_id =:via_id")
    fun getViaMod(via_id: String): Via

    @Query("delete from vie")
    fun deleteDatabaseVie()

    @Query("SELECT * FROM vie WHERE falesiaIdFk=:falesiaId AND settore=:settore AND numero=:numero")
    fun getIdViaFalesiaSettoreNumero(falesiaId: String, settore: String, numero: Int): Via

}


@Dao
interface FalesiaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(falesia: Falesia)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(falesia: Falesia)

    @Delete
    suspend fun delete(falesia: Falesia)

    @Query("SELECT * FROM falesia")
    fun getAllFalesie(): Flow<List<Falesia>>

    @Query("SELECT nome FROM falesia WHERE falesia_id =:falesia_id")
    fun getNomeFalesia(falesia_id: String): String

    @Query("SELECT nome FROM falesia")
    fun getNomiFalesie(): List<String>

    @Query("SELECT * FROM falesia")
    fun getAllFalesieNew(): List<Falesia>

    @Query("delete from falesia")
    fun deleteDatabaseFalesia()


}
