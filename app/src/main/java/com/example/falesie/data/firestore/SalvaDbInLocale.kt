package com.example.falesie.data.firestore

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.falesie.Aggiorna
import com.example.falesie.FalesieViewModel
import com.example.falesie.FalesieViewModelFactory
import com.example.falesie.MainActivity
import com.example.falesie.data.room.models.Via


@Composable
fun salvaDbVieInLocale(factory: FalesieViewModelFactory) {
    //USATA PER SALVARE IL DATABASE DI RETE NEL DATABASE LOCALE
    var caricamentoCompletato by remember { mutableStateOf(false) }
    FirestoreClass().leggiTutteLeVie(object : Aggiorna {
        override fun aggiorna() {
            Log.i("VIE LETTE ELSE", MainActivity.listaVie.size.toString())
            caricamentoCompletato = true
        }
    })
    if (caricamentoCompletato == true) {
        //compilaListaFalesie(paddingValues)
        //RecyclerView(paddingValues, navController)
        //SALVA LE VIE NEL DATABASE LOCALE ROOM
        salvaVieInLocale(factory)
    }
}

@Composable
fun salvaDbFalesieInLocale(factory: FalesieViewModelFactory) {
    //USATA PER SALVARE IL DATABASE DI RETE NEL DATABASE LOCALE
    var caricamentoCompletato by remember { mutableStateOf(false) }
    FirestoreClass().leggiTutteLeFalesie(object : Aggiorna {
        override fun aggiorna() {
            Log.i("FALESIE LETTE ELSE", MainActivity.listaFalesie.size.toString())
            caricamentoCompletato = true
        }
    })
    if (caricamentoCompletato == true) {

        //SALVA LE VIE NEL DATABASE LOCALE ROOM
        salvaFalesieInLocale(factory)
    }
}


@Composable
fun salvaVieInLocale(
    factory: FalesieViewModelFactory,
    falesieViewModel : FalesieViewModel = viewModel(factory = factory),
) {

    for (i in MainActivity.listaVie) {
        var viaRoom = Via(
            id = i.id,
            viaName = i.nome,
            settore = i.settore,
            numero = i.numero,
            falesiaIdFk = i.falesia,
            grado = i.grado,
            protezioni = i.protezioni,
            altezza = i.altezza,
            immagine = i.immagine,
            isChecked = false
        )
        falesieViewModel.addVia(viaRoom)
        //repository.insertVia(viaRoom)
        //runBlocking { repository.insertVia(viaRoom) }
    }
}

@Composable
fun salvaFalesieInLocale(
    factory: FalesieViewModelFactory,
    falesieViewModel : FalesieViewModel = viewModel(factory = factory),) {
    for (i in MainActivity.listaFalesie) {
        var falesiaRoom = com.example.falesie.data.room.models.Falesia(
            id = i.id,
            nome = i.nome,
            descrizione = i.descrizione,
            latitudine = i.latitudine,
            longitudine = i.longitudine,
            stagioni = i.stagioni,
            altitudine = i.altitudine,
            primavera = i.primavera,
            estate = i.estate,
            autunno = i.autunno,
            inverno = i.inverno
        )
        falesieViewModel.addFalesia(falesiaRoom)
        //repository.insertVia(viaRoom)
        //runBlocking { repository.insertFalesia(falesiaRoom) }
    }
}