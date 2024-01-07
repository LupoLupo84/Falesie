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
//    val vieNellaFalesia = repositoryVie.getVieFalesia(falesiaCorrenteId)
    val vieNellaFalesia = repositoryVie.getVieFalesia(Constants.FALESIACORRENTEID)
    //val vieNellaFalesiaSettore = repositoryVie.getVieFalesiaSettore(Constants.FALESIACORRENTEID,Constants.SETTORECORRENTE)

    val vieNellaFalesiaSettore = if (Constants.SETTORECORRENTE == "Tutti i settori"){
        repositoryVie.getVieFalesia(Constants.FALESIACORRENTEID)
    }else{
        repositoryVie.getVieFalesiaSettore(Constants.FALESIACORRENTEID,Constants.SETTORECORRENTE)
    }

    //val settoriFalesia: MutableList<String> = calcolaSettori(vieNellaFalesiaSettore)


//    val vieNellaFalesiaSettore = if (settoreCorrente == "Tutti i settori"){
//        Log.d("WiewModel_IF", settoreCorrente)
//        repositoryVie.getVieFalesia(falesiaCorrenteId)
//    }else{
//        Log.d("WiewModel_ELSE", settoreCorrente)
//        repositoryVie.getVieFalesiaSettore(falesiaCorrenteId,settoreCorrente)
//    }






    
    
    
    
    
    
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

//    fun vieNellaFalesia(falesia: String): Flow<List<Via>> {
//        Log.d("nel viewmodel", "nome falesia ${falesia}")
//        return repositoryVie.getVieFalesia(falesia)
//    }


//    @Composable
//    fun titoloSettori(falesia: String): Flow<List<String>> {
//        //var vieNellaFalesia = repositoryVie.getVieFalesia(falesia).collectAsState(initial = emptyList())
//        var vieNellaFalesia = repositoryVie.getVieFalesia(falesia).collectAsState(initial = emptyList())
//        var settoriNellaFalesia = emptyList<String>()
//
////        var nomeSettore = "tutti i settori"
////        settoriNellaFalesia.add(nomeSettore)
////        settoriNellaFalesia.add("1 settore")
////        settoriNellaFalesia.add("2 settore")
////        settoriNellaFalesia.add("3 settore")
////        settoriNellaFalesia.add("4 settore")
//
//
////        for (i in 0 until vieNellaFalesia){
////            Log.d("via numero ${i}", vieNellaFalesia.value[i].settore)
////            if (vieNellaFalesia.value[i].settore != nomeSettore){
////                settoriNellaFalesia.add(nomeSettore)
////                //settoriNellaFalesia.add(vieNellaFalesia.value[i].settore)
////                nomeSettore = vieNellaFalesia.value[i].settore
////            }
////        }
//        return settoriNellaFalesia
//    }



}