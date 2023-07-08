package com.example.falesie.room

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falesie.MainActivity
import com.example.falesie.MainActivity.Companion.listaVie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViarViewModel(
    private val dao: ViarDao
):ViewModel() {

    private val _state = MutableStateFlow(ViarState())

    fun onEvent(event: ViarEvent){
        when(event){
            is ViarEvent.DeleteViar ->{
                
                viewModelScope.launch {
                    dao.deleteViar(event.contact)
                }
            }
            
            is ViarEvent.SetAltezza -> {
                _state.update { it.copy(
                    altezza = event.altezza
                ) }
            }
            is ViarEvent.SetFalesia -> {
                _state.update { it.copy(
                    falesia = event.falesia
                ) }
            }
            is ViarEvent.SetGrado -> {
                _state.update { it.copy(
                    grado = event.grado
                ) }
            }
            is ViarEvent.SetId -> {
                _state.update { it.copy(
                    id = event.id
                ) }
            }
            is ViarEvent.SetImmagine -> {
                _state.update { it.copy(
                    immagine = event.immagine
                ) }
            }
            is ViarEvent.SetNome -> {
                _state.update { it.copy(
                    nome = event.nome
                ) }
            }
            is ViarEvent.SetNumero -> {
                _state.update { it.copy(
                    numero = event.numero
                ) }
            }
            is ViarEvent.SetProtezioni -> {
                _state.update { it.copy(
                    protezioni = event.protezioni
                ) }
            }
            is ViarEvent.SetSettore -> {
                _state.update { it.copy(
                    settore = event.settore
                ) }
            }
            ViarEvent.SaveViar -> {
                Log.d("ROOM", "onEvent SALVA")
                Log.d("ROOM", "vie presenti in listavie ${listaVie.size}")
                for (i in listaVie){
                    val viar = Viar(
                        id = i.id,
                        falesia = i.falesia,
                        settore = i.settore,
                        numero = i.numero,
                        nome = i.nome,
                        grado = i.grado,
                        protezioni = i.protezioni,
                        altezza = i.altezza,
                        immagine = i.immagine
                    )
                    Log.d("ROOM", "inserimento via ${i.nome}")

                    viewModelScope.launch {
                        dao.upsertViar(viar)
                    }

                }

            }
            
        }

    }

    private fun carica() {
        Log.d("ROOM", "arrivati nella funzione")
        Log.d("ROOM", "vie presenti in listavie ${listaVie.size}")
        for (i in listaVie){
            val viar = Viar(
                id = i.id,
                falesia = i.falesia,
                settore = i.settore,
                numero = i.numero,
                nome = i.nome,
                grado = i.grado,
                protezioni = i.protezioni,
                altezza = i.altezza,
                immagine = i.immagine
            )
            Log.d("ROOM", "inserimento via ${i.nome}")

            viewModelScope.launch {
//                    dao.upsertContact(contact)
                dao.upsertViar(viar)
            }
            //onEvent(ViarEvent.SaveViar)
        }
    }


}