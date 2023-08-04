package com.example.falesie.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.falesie.Graph
import com.example.falesie.data.room.ItemsWithStoreAndList
import com.example.falesie.data.room.models.Falesia
import com.example.falesie.data.room.models.ShoppingList
import com.example.falesie.data.room.models.Store
import com.example.falesie.data.room.models.Via
import com.example.falesie.ui.Category
import com.example.falesie.ui.Utils
import com.example.falesie.ui.detail.DetailViewModel
import com.example.falesie.ui.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class FalesieViewModel
constructor(
    private val repository: Repository = Graph.repository
): ViewModel() {
    //var state by mutableStateOf(DettagliVia())
    var state by mutableStateOf(VieState())
        private set

    var stateFalesie by mutableStateOf(FalesieState())
        private set

    init {
        getVie()
        getFalesie()
    }









    private val _listaLetta = mutableStateListOf(Graph.repository.storeVie)

    var listaLetta = _listaLetta




    fun onItemChange(newValue: String) {
        //state = state.copy(nome = newValue)

    }

    fun addVia(viaDaInserire:Via) {
        viewModelScope.launch {
            repository.insertVia(
                viaDaInserire
            )
        }
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

    fun getFalesie() {
        viewModelScope.launch {
            repository.storeFalesie.collectLatest {
                stateFalesie = stateFalesie.copy(
                    items = it
                    //storeListVie = it
                )
            }
        }
    }


}







@Suppress("UNCHECKED_CAST")
class FalesieViewModelFactory(): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FalesieViewModel() as T
    }
}


data class DettagliVia(                 // classe uguale a Via
    val storeListVie: List<Via> = emptyList(),
    var id: String = "",
    var falesia: String = "",
    val settore: String = "",
    val numero: Int = 0,
    var nome: String = "",
    val grado: String = "",
    val protezioni: Int = 0,
    val altezza: Int = 0,
    val immagine: String = ""
    )

data class VieState(
    val items: List<Via> = emptyList(),
)

data class FalesieState(
    val items: List<Falesia> = emptyList(),
)