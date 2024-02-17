package com.example.falesie

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falesie.data.room.FalesiaRepository
import com.example.falesie.data.room.ViaRepository
import com.example.falesie.data.room.models.Via
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class FalesieViewModel(private val repositoryFalesie: FalesiaRepository,private val repositoryVie: ViaRepository) : ViewModel(){
    val settoreCorrente by mutableStateOf(Constants.SETTORECORRENTE)
    val falesiaCorrenteId by mutableStateOf(Constants.FALESIACORRENTEID)

    val vieList = repositoryVie.getAllVie()
    val falesieList = repositoryFalesie.getAllFalesie()
    val vieNellaFalesia = repositoryVie.getVieFalesia(Constants.FALESIACORRENTEID)

    val vieNellaFalesiaSettore = if (Constants.SETTORECORRENTE == "Tutti i settori"){
        repositoryVie.getVieFalesia(Constants.FALESIACORRENTEID)
    }else{
        repositoryVie.getVieFalesiaSettore(Constants.FALESIACORRENTEID,Constants.SETTORECORRENTE)
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




}