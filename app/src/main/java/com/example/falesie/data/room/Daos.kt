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
interface ViaDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(via: Via)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(via: Via)

    @Delete
    suspend fun delete(via: Via)

    @Query("SELECT * FROM vie")
    fun getAllVie():Flow<List<Via>>

    @Query("SELECT * FROM vie WHERE via_id =:via_id")
    fun getVia(via_id:Int):Flow<Via>

    @Query("SELECT * FROM vie WHERE falesiaIdFk =:falesia ORDER BY numero")
    fun getVieFalesia(falesia:String):Flow<List<Via>>


    @Query("SELECT * FROM vie WHERE falesiaIdFk =:falesia AND settore =:settore ORDER BY numero")
    fun getVieFalesiaSettore(falesia:String,settore:String):Flow<List<Via>>


}


@Dao
interface FalesiaDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(falesia: Falesia)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(falesia: Falesia)

    @Delete
    suspend fun delete(falesia: Falesia)

    @Query("SELECT * FROM falesia")
    fun getAllFalesie():Flow<List<Falesia>>

    @Query("SELECT * FROM falesia WHERE falesia_id =:falesia_id")
    fun getFalesia(falesia_id:Int):Flow<Falesia>

}
