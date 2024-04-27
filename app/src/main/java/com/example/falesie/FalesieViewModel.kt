package com.example.falesie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falesie.data.room.FalesiaRepository
import com.example.falesie.data.room.ViaRepository
import com.example.falesie.data.room.models.Falesia
import com.example.falesie.data.room.models.Via
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FalesieViewModel(private val repositoryFalesie: FalesiaRepository,private val repositoryVie: ViaRepository) : ViewModel(){
//    val settoreCorrente by mutableStateOf(Constants.SETTORECORRENTE)
//    val falesiaCorrenteId by mutableStateOf(Constants.FALESIACORRENTEID)


    val nomeFalesia = MutableLiveData<String>()
    val listaNomiFalesie = MutableLiveData<List<String>>()
    val listaNomiSettore = MutableLiveData<List<String>>()
    val viaDaMod = MutableLiveData<Via>()

    val vieList = MutableLiveData<List<Via>>()
    val falesieList = MutableLiveData<List<Falesia>>()
    val vieNellaFalesia = MutableLiveData<List<Via>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        getVieNellaFalesia()
    }


    fun getVieNellaFalesia(){
        coroutineScope.launch(Dispatchers.Main){
            vieNellaFalesia.value = asyncGetVieNellaFalesia().await()
        }
    }
    private fun asyncGetVieNellaFalesia():Deferred<List<Via>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async repositoryVie.getVieFalesia(Constants.FALESIACORRENTEID)
        }





    fun getViaIdMod(via_id: String) {
        coroutineScope.launch(Dispatchers.Main){
            viaDaMod.value = asyncGetViaIdMod(via_id).await()
        }
    }

    private fun asyncGetViaIdMod(via_id: String):Deferred<Via> =
        coroutineScope.async(Dispatchers.IO) {
            return@async repositoryVie.getVia(via_id)
        }









    fun addVia(via: Via){
        Log.d("nel viewmodel", "aggiungi via")
        viewModelScope.launch(Dispatchers.IO) {
            repositoryVie.insert(via)
        }
    }

    fun addFalesia(falesia: com.example.falesie.data.room.models.Falesia){
        Log.d("nel viewmodel", "aggiungi falesia")
        viewModelScope.launch(Dispatchers.IO) {
            repositoryFalesie.insert(falesia)
        }
    }



    fun getNomeFalesia(falesia_id: String){
        coroutineScope.launch(Dispatchers.Main) {
            //Log.d("CHIAMATA", "getfalesia1")
            nomeFalesia.value = asyncFindNomeFalesia(falesia_id).await()
            //Log.d("CHIAMATA", "getfalesia1 _ RITORNO")
            //Log.d("CHIAMATA -> valore ", nomeFalesia.value.toString())
        }
    }
    private fun asyncFindNomeFalesia(name:String): Deferred<String> =
        coroutineScope.async(Dispatchers.IO){
            //Log.d("CHIAMATA", "asyncFind")
            return@async repositoryFalesie.getNomeFalesia(name)
        }



    fun getNomiFalesie(){
        coroutineScope.launch(Dispatchers.Main){
            listaNomiFalesie.value = asyncFindNomiFalesie().await()
        }
    }
    private fun asyncFindNomiFalesie():Deferred<List<String>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async repositoryFalesie.getNomiFalesie()
        }


    fun getNomiSettore(falesia_id: String){
        coroutineScope.launch(Dispatchers.Main){
            listaNomiSettore.value = asyncFindNomiSettore(falesia_id).await()
        }
    }
    private fun asyncFindNomiSettore(falesia_id: String):Deferred<List<String>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async repositoryVie.getNomiSettore(falesia_id)
        }




    fun getVieList(){
        coroutineScope.launch(Dispatchers.Main){
            vieList.value = asyncGetVieList().await()
        }
    }
    private fun asyncGetVieList():Deferred<List<Via>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async repositoryVie.getAllVie()
        }


    fun getFalesieList(){
        coroutineScope.launch(Dispatchers.Main){
            falesieList.value = asyncGetFalesieList().await()
        }
    }
    private fun asyncGetFalesieList():Deferred<List<Falesia>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async repositoryFalesie.getAllFalesieNew()
        }


    












}