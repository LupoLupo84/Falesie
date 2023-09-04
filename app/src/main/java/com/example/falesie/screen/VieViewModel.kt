package com.example.falesie.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.falesie.Graph
import com.example.falesie.data.room.models.Via
import com.example.falesie.ui.repository.Repository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class VieViewModel
constructor(
    private val repository: Repository = Graph.repository
): ViewModel() {


//    var state by  mutableListOf(ArrayList<Via>())
//        private set

    var state by mutableStateOf(VieState())
        private set

    var settoriState by mutableStateOf(ArrayList<String>())
        private set




//    var vieNellaFalesia by mutableStateOf(VieFalesiaState())
//        private set
//
//    var vieDaVisualizzare: ArrayList<Via> = ArrayList()


    init {
       // getVie()
    }



//    if (settori.size <= 1) {
//        for (i in viewModelVia.state.items) {
//            if (i.settore != settore) {
//                settore = i.settore
//                settori.add(i.settore)
//            }
//        }
//        settori.add("Tutti i settori")
//    }
//val settori: MutableList<String> = arrayListOf()

    fun getSettoriFalesia(idFalesia:String){
        Log.d("SETTORI FALESIA", "nella funzione")
        var temporaneo by mutableStateOf(ArrayList<String>())
        var settore= "Tutti i settori"
        temporaneo.add("Tutti i settori")
        viewModelScope.launch {
            //val temp = repository.getVieFalesia(idFalesia)
            repository.getVieFalesia(idFalesia).collectLatest {
                for (i in 0..it.size-1){
                    if (it[i].settore != settore){
                        settore = it[i].settore
                        temporaneo.add(it[i].settore)
                        settoriState = ArrayList(temporaneo.distinct())         //toglie tutti i duplicati
                        //Log.d("Settori della falesia", it[i].settore)
                    }
                }
            }
        }
    }




    fun getVieFalesia(idFalesia :String) {
        viewModelScope.launch {
            val temp = repository.getVieFalesia(idFalesia)
            temp.collectLatest {
                state = state.copy(
                    items = it
                )
            }
        }
        getSettoriFalesia(idFalesia)
    }

    fun getVieFalesiaSettore(idFalesia :String, settore : String){
        Log.d("STATE", "nella funzione getVieFalesiaSettore")
        if (settore == "Tutti i settori"){
            getVieFalesia(idFalesia)
        }else{
            viewModelScope.launch {
                //val temp = repository.getVieFalesiaSettore(idFalesia, settore)
                //temp.collectLatest {
                repository.getVieFalesiaSettore(idFalesia, settore).collectLatest {
                    state = state.copy(
                        items = it
                    )
                }
            }
        }
        Log.d("STATE", "settore selezionato ${settore}")
        Log.d("STATE", "dimensione delle vie ${state.items.size.toString()}")

    }



    fun getVie() {
        viewModelScope.launch {
            repository.storeVie.collectLatest {
                state = state.copy(
                    items = it
                    //storeListVie = it
                )
            }
        }
    }

}




data class VieFalesiaState(
    val items: List<Via> = emptyList(),
)


@Suppress("UNCHECKED_CAST")
class VieViewModelFactory(): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VieViewModel() as T
    }
}