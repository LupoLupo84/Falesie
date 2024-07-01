package com.example.falesie.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.falesie.FalesieViewModel
import com.example.falesie.FalesieViewModelFactory
import com.example.falesie.MainActivity.Companion.immagineViaPassata
import com.example.falesie.data.firestore.FirestoreClass
import com.example.falesie.data.room.models.Via
import com.example.falesie.tool.EditaDropdown
import com.example.falesie.tool.EditaTesto
import com.example.falesie.tool.listaGradi
import com.example.falesie.tool.numeri50
import kotlinx.coroutines.delay
import java.io.File


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AggiungiViaScreen(
    navController: NavHostController,
    //idVia: String,
    factory: FalesieViewModelFactory,
    falesieViewModel: FalesieViewModel = viewModel(factory = factory),
    //vieNellaFalesia: State<List<Via>>,
    //vieNellaFalesia: List<Via>,
    //viaDaModificare: Via
) {
    //val viaDaModificare by falesieViewModel.viaDaMod.observeAsState()
    //val viaDaModificare = viaDaMod!!
    var via: com.example.falesie.data.firestore.model.Via = com.example.falesie.data.firestore.model.Via(
        id = "",
        nome = "",
        settore = "",
        numero = 0,
        falesia = "",
        grado = "",
        protezioni = 0,
        altezza = 0,
        immagine = "",
    )


    falesieViewModel.getNomiFalesie()
//    falesieViewModel.getNomiSettore(viaDaModificare.falesiaIdFk)
//    falesieViewModel.getNomeFalesia(viaDaModificare.falesiaIdFk)                             //chiama la funzione per leggere il nome della falesia tramite id
    val nomeFalesiaViewModel by falesieViewModel.nomeFalesia.observeAsState()       //osserva quando il risultato della funzione viene aggiornato
    val listaNomiFalesie by falesieViewModel.listaNomiFalesie.observeAsState()
    val listaNomiSettoreViewModel by falesieViewModel.listaNomiSettore.observeAsState()
    val falesia by falesieViewModel.falesia.observeAsState()


    //val test by falesieViewModel.viaDaMod2.observeAsState()
    val prossimavia by falesieViewModel.prossimavia.observeAsState()
    val precedentevia by falesieViewModel.precedentevia.observeAsState()
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    //var larghezzaImmagine = 0.dp
    val larghezzaImmagine = if (screenHeight >= 1000){
        ((screenWidth / 100)*90).dp                                 //90
    }else{
        ((screenWidth / 100)*55).dp
    }
    val larghezzaFrecce = ((screenWidth / 100)*10).dp           //10







    val listaNomiSettore = listaNomiSettoreViewModel?.distinct()


//    Log.d("LISTA NOMI FALESIA", listaNomiFalesie?.size.toString())
//
//    viaDaModificare?.let { Log.d("SETTORE VIA", it.settore) }


    //var nomeFalesia = ""


//    val nomeFalesia = nomeFalesiaViewModel
//
//    //LEGGI LA VIA SUCCESSIVA
//    falesieViewModel.getIdFromFalesiaSettoreNumeroSucc(
//        falesiaId = viaDaModificare.falesiaIdFk,
//        settore = viaDaModificare.settore,
//        numero = viaDaModificare.numero + 1
//    )
//
//    //LEGGI LA VIA PRECEDENTE
//    falesieViewModel.getIdFromFalesiaSettoreNumeroPrec(
//        falesiaId = viaDaModificare.falesiaIdFk,
//        settore = viaDaModificare.settore,
//        numero = viaDaModificare.numero - 1
//    )








//        if (viaDaModificare.falesiaIdFk == "") {
//            viaDaModificare.falesiaIdFk = viaDaModificare.falesiaIdFk
//            //falesieViewModel.getFalesia(viaDaModificare.falesiaIdFk)
//            Log.d("FALESIA ID CORRENTE", viaDaModificare.falesiaIdFk)
//        }


        val scrollBehaivor = TopAppBarDefaults.pinnedScrollBehavior()
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)






        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheetMenu(navController = navController)
            }
        ) {

            Scaffold(
                modifier = Modifier
                    .nestedScroll(scrollBehaivor.nestedScrollConnection),
                topBar = {
                    TopAppBarFalesia(
                        scrollBehaivor = scrollBehaivor,
                        scope = scope,
                        drawerState = drawerState,
                        titolo = "Aggiungi via",
                    )
                }
            ) {
                val secondPadding = it.calculateTopPadding()
                val columnHeightDp by remember { mutableStateOf(0.dp) }
                val localDensity = LocalDensity.current
                //val listSorted = vieNellaFalesia.toList()


                Surface(
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(
                            top = secondPadding, // + columnHeightDp,
                            start = 4.dp,
                            end = 4.dp
                        ),
                    shape = RoundedCornerShape(10.dp),
                    shadowElevation = 2.dp
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                //.padding(start = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)

                            ) {



                                        listaNomiFalesie?.let { it1 ->
                                            falesieViewModel.getFalesiaFromName(
                                            EditaDropdown(
                                                testo = "falesia",
                                                selezioneAttuale = "          ",
                                                //selezioneMenu = emptyList()
                                                selezioneMenu = it1
                                            )
                                            )
                                        }










//                                via.settore =
//                                    listaNomiSettore?.let { it1 ->
//                                        EditaDropdown(
//                                            testo = "settore",
//                                            selezioneAttuale = via.settore,
//                                            selezioneMenu = it1
//                                        )
//                                    }.toString()

                                via.settore =
                                    EditaTesto(
                                        "settore",
                                        via.settore
                                    )



                                via.numero =
                                    EditaDropdown(
                                        "numero",
                                        via.numero.toString(),
                                        numeri50
                                    ).toInt()


                                via.nome =
                                    EditaTesto(
                                        "nome",
                                        via.nome
                                    )




                                Row {
                                    Column(
                                        modifier = Modifier.weight(1F)
                                    ) {
                                        via.grado =
                                            EditaDropdown(
                                                "grado",
                                                via.grado,
                                                listaGradi
                                            )
                                    }
                                    Column(
                                        modifier = Modifier.weight(1F)
                                    ) {


                                        via.altezza =
                                            EditaDropdown(
                                                "altezza",
                                                via.altezza.toString(),
                                                numeri50
                                            ).toInt()
                                    }
                                    Column(
                                        modifier = Modifier.weight(1F)
                                    ) {
                                        Log.d("protezioni", via.protezioni.toString())

                                        via.protezioni =
                                            EditaDropdown(
                                                "protezioni",
                                                via.protezioni.toString(),
                                                numeri50
                                            ).toInt()
                                    }

                                }
                                Row {
                                    Column {

                                        if (via.immagine == ""){
                                            via.immagine =
                                                EditaTesto(
                                                    "nome immagine",
                                                    //viaDaModificare.immagine
                                                    immagineViaPassata
                                                )
                                        }else {

                                            via.immagine =
                                                EditaTesto(
                                                    "nome immagine",
                                                    via.immagine
                                                    //immagineViaPassata
                                                )
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
//                                    Column(
//                                        modifier = Modifier
//                                            .wrapContentWidth()
//                                            .weight(0.1f)                                    ) {
//                                        IconButton(onClick = {
//                                            //salvaViaNelDatabase(viaDaModificare)
//                                            //LEGGI LA VIA PRECEDENTE
//                                            if (precedentevia?.id?.isNotEmpty() == true){
//                                                navController.popBackStack()
//                                                val route = "${"AggiornaViaScreen"}/${precedentevia!!.id}"
//                                                //val route = "${"AggiornaViaScreen"}/${falesieViewModel.prossimavia.value?.id}"
//                                                navController.navigate(route)
//                                            }
//
//
//                                        }) {
//                                            Icon(
//                                                modifier = Modifier.size(larghezzaFrecce),            //50.dp
//                                                imageVector = Icons.Filled.ArrowCircleLeft,
//                                                contentDescription = "ArrowBack"
//                                            )
//                                        }
//                                    }           //FRECCIA INDIETRO

                                    Column(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .weight(0.9f)
                                    ) {
                                        val myDir: File =
                                            File(LocalContext.current.filesDir, "falesie")
                                        val new_file: File =
                                            File("$myDir" + File.separator + via.immagine + ".webp")
                                        val immagine = new_file.absolutePath.toString()
                                        //     "https://www.gyfted.me/_next/image?url=%2Fimg%2Fcharacters%2Fmilhouse-van-houten.png&w=640&q=75"
                                        Log.d("IMMAGINE", new_file.absolutePath)


                                        Image(
                                            modifier = Modifier
                                                //.size(250.dp),                              //250
                                                .size(larghezzaImmagine),
                                            painter = rememberAsyncImagePainter(
                                                ImageRequest.Builder(LocalContext.current)
                                                    .data(immagine)
                                                    .apply(block = fun ImageRequest.Builder.() {
                                                        crossfade(false)
                                                    }).build()
                                            ),
                                            contentDescription = "description",
                                            contentScale = ContentScale.Fit
                                        )
                                    }           //IMMAGINE

//                                    Column(
//                                        modifier = Modifier
//                                            .wrapContentWidth()
//                                            .weight(0.1f)
//                                    ) {
//                                        IconButton(onClick = {
//                                            if (prossimavia?.id?.isNotEmpty() == true){
//                                                navController.popBackStack()
//                                                val route = "${"AggiornaViaScreen"}/${prossimavia!!.id}"
//                                                //val route = "${"AggiornaViaScreen"}/${falesieViewModel.prossimavia.value?.id}"
//                                                navController.navigate(route)
//                                            }
//
//
//
//
//
//
//                                        }) {
//                                            Icon(
//                                                modifier = Modifier.size(larghezzaFrecce),            //50.dp
//                                                imageVector = Icons.Filled.ArrowCircleRight,
//                                                contentDescription = "ArrowNext"
//                                            )
//                                        }
//                                    }           //FRECCIA AVANTI

                                }

                                Row {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        TextButton(
                                            onClick = {

                                                //TODO AGGIORNARE FALESIA SU FIRESTORE

                                                //falesieViewModel.getFalesiaFromName(via.falesia)


                                                falesia?.let { it ->
                                                    via.falesia = it.id

                                                    Log.d("Falesia id", via.falesia)
                                                    Log.d("Settore", via.settore)
                                                    Log.d("Nome", via.nome)
                                                    Log.d("Grado", via.grado)
                                                    Log.d("Altezza", via.altezza.toString())
                                                    Log.d(
                                                        "Protezioni",
                                                        via.protezioni.toString()
                                                    )
                                                    Log.d("Immagine", via.immagine)







                                                    FirestoreClass().creaVia(via, context)
                                                    Log.d("Via inserita", "CORRETTAMENTE")

                                                    //Imposta il ricarico delle falesie al prossimo riavvio
                                                    val setAggiorna: HashMap<String, Any> = hashMapOf("aggiorna" to true)
                                                    FirestoreClass().updateUserProfileData(
                                                        setAggiorna
                                                    )

                                                    navController.popBackStack()

                                                }


                                                //immagineViaPassata = via.immagine



                                            }

                                        ) {
                                            Text(text = "Aggiungi via")
                                        }

                                        //prossimavia?.let { it1 -> Text(text = it1.viaName) }
                                        Text( screenHeight.toString())
                                    }

                                }







                            }

                        }

                    }

                }

            }


        }






}
















