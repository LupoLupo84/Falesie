package com.example.falesie

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
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


class FalesieViewModel(
    private val repositoryFalesie: FalesiaRepository,
    private val repositoryVie: ViaRepository
) : ViewModel() {


    val nomeFalesia = MutableLiveData<String>()
    val listaNomiFalesie = MutableLiveData<List<String>>()
    val listaNomiSettore = MutableLiveData<List<String>>()
    val viaDaMod = MutableLiveData<Via>()

    var vieList = MutableLiveData<List<Via>>()
    var falesieList = MutableLiveData<List<Falesia>>()
    val vieNellaFalesia = MutableLiveData<List<Via>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    private val _viaDaMod2 = MutableLiveData<Via>()
    val viaDaMod2: LiveData<Via>
        get() = _viaDaMod2
    val prossimavia = MutableLiveData<Via>()
    val precedentevia = MutableLiveData<Via>()

    init {
        getVieNellaFalesia()
        getVieList()
        getFalesieList()
    }

    fun svuotaDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryFalesie.deleteDatabaseFalesia()
            repositoryVie.deleteDatabaseVie()
        }
    }

    fun getVieNellaFalesia() {
        coroutineScope.launch(Dispatchers.Main) {
            vieNellaFalesia.value = asyncGetVieNellaFalesia().await()
        }
    }

    private fun asyncGetVieNellaFalesia(): Deferred<List<Via>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async repositoryVie.getVieFalesia(Constants.FALESIACORRENTEID)
        }

    fun getViaIdMod(via_id: String) {
        coroutineScope.launch(Dispatchers.Main) {
            viaDaMod.value = asyncGetViaIdMod(via_id).await()
            _viaDaMod2.value = asyncGetViaIdMod(via_id).await()
        }
    }

    private fun asyncGetViaIdMod(via_id: String): Deferred<Via> =
        coroutineScope.async(Dispatchers.IO) {
            return@async repositoryVie.getVia(via_id)
        }

    fun addVia(via: Via) {
        Log.d("nel viewmodel", "aggiungi via")
        viewModelScope.launch(Dispatchers.IO) {
            repositoryVie.insert(via)
        }
    }

    fun addFalesia(falesia: com.example.falesie.data.room.models.Falesia) {
        Log.d("nel viewmodel", "aggiungi falesia")
        viewModelScope.launch(Dispatchers.IO) {
            repositoryFalesie.insert(falesia)
        }
    }


    fun getNomeFalesia(falesia_id: String) {
        coroutineScope.launch(Dispatchers.Main) {
            //Log.d("CHIAMATA", "getfalesia1")
            nomeFalesia.value = asyncFindNomeFalesia(falesia_id).await()
            //Log.d("CHIAMATA", "getfalesia1 _ RITORNO")
            //Log.d("CHIAMATA -> valore ", nomeFalesia.value.toString())
        }
    }

    private fun asyncFindNomeFalesia(name: String): Deferred<String> =
        coroutineScope.async(Dispatchers.IO) {
            //Log.d("CHIAMATA", "asyncFind")
            return@async repositoryFalesie.getNomeFalesia(name)
        }


    fun getNomiFalesie() {
        coroutineScope.launch(Dispatchers.Main) {
            listaNomiFalesie.value = asyncFindNomiFalesie().await()
        }
    }

    private fun asyncFindNomiFalesie(): Deferred<List<String>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async repositoryFalesie.getNomiFalesie()
        }


    fun getNomiSettore(falesia_id: String) {
        coroutineScope.launch(Dispatchers.Main) {
            listaNomiSettore.value = asyncFindNomiSettore(falesia_id).await()
        }
    }

    private fun asyncFindNomiSettore(falesia_id: String): Deferred<List<String>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async repositoryVie.getNomiSettore(falesia_id)
        }


    fun getVieList() {
        coroutineScope.launch(Dispatchers.Main) {
            vieList.value = asyncGetVieList().await()
        }
    }

    private fun asyncGetVieList(): Deferred<List<Via>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async repositoryVie.getAllVie()
        }


    fun getFalesieList() {
        coroutineScope.launch(Dispatchers.Main) {
            falesieList.value = asyncGetFalesieList().await()
        }
    }

    private fun asyncGetFalesieList(): Deferred<List<Falesia>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async repositoryFalesie.getAllFalesieNew()
        }


    fun getIdFromFalesiaSettoreNumeroSucc(falesiaId: String, settore: String, numero: Int){
        Log.d("nella funzione numero via ", numero.toString())
        coroutineScope.launch(Dispatchers.Main) {
            prossimavia.value = asyncGetIdFromFalesiaSettoreNumero(falesiaId = falesiaId, settore = settore, numero = numero).await()
            //viaDaMod.value = asyncGetIdFromFalesiaSettoreNumero(falesiaId = falesiaId, settore = settore, numero = numero).await()
            //repositoryVie.getIdViaFalesiaSettoreNumero(falesiaId = falesiaId, settore = settore, numero = numero)
//            Log.d("falesia", prossimavia.value!!.falesiaIdFk)
//            Log.d("settore", prossimavia.value!!.settore)
//            Log.d("nome", prossimavia.value!!.viaName)
//            Log.d("numero", prossimavia.value!!.numero.toString())
//            Log.d("id", prossimavia.value!!.id)
        }
    }

    fun getIdFromFalesiaSettoreNumeroPrec(falesiaId: String, settore: String, numero: Int){
        Log.d("nella funzione numero via ", numero.toString())
        coroutineScope.launch(Dispatchers.Main) {
            precedentevia.value = asyncGetIdFromFalesiaSettoreNumero(falesiaId = falesiaId, settore = settore, numero = numero).await()
        }
    }

    suspend fun asyncGetIdFromFalesiaSettoreNumero(falesiaId:String, settore:String, numero:Int): Deferred<Via> =
        coroutineScope.async(Dispatchers.IO) {
            return@async repositoryVie.getIdViaFalesiaSettoreNumero(falesiaId = falesiaId, settore = settore, numero = numero)
        }





}