package com.example.falesie.screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.falesie.FalesieViewModel
import com.example.falesie.FalesieViewModelFactory
import com.example.falesie.data.room.models.Via
import com.example.falesie.tool.listaGradi
import com.example.falesie.tool.numeri50


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AggiornaViaScreen(
    navController: NavHostController,
    idVia: String,
    factory: FalesieViewModelFactory,
    falesieViewModel: FalesieViewModel = viewModel(factory = factory),
    //vieNellaFalesia: State<List<Via>>,
    vieNellaFalesia: List<Via>,
    viaDaModificare: Via
) {
    falesieViewModel.getNomiFalesie()
    falesieViewModel.getNomiSettore(viaDaModificare.falesiaIdFk)
    falesieViewModel.getNomeFalesia(viaDaModificare.falesiaIdFk)                            //chiama la funzione per leggere il nome della falesia tramite id
    val nomeFalesiaViewModel by falesieViewModel.nomeFalesia.observeAsState()       //osserva quando il risultato della funzione viene aggiornato
    val listaNomiFalesie by falesieViewModel.listaNomiFalesie.observeAsState()
    val listaNomiSettoreViewModel by falesieViewModel.listaNomiSettore.observeAsState()

    val listaNomiSettore = listaNomiSettoreViewModel?.distinct()


    Log.d("LISTA NOMI FALESIA", listaNomiFalesie?.size.toString())

    Log.d("SETTORE VIA", viaDaModificare.settore)


    var nomeFalesia = ""
    nomeFalesiaViewModel?.let {
        Log.d(
            "RISPOSTA NEL MAIN",
            it
        )
        nomeFalesia = it
    }





    if (viaDaModificare.falesiaIdFk == "") {
        viaDaModificare.falesiaIdFk = viaDaModificare.falesiaIdFk
        //falesieViewModel.getFalesia(viaDaModificare.falesiaIdFk)
        Log.d("FALESIA ID CORRENTE", viaDaModificare.falesiaIdFk)
    }


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
            modifier = Modifier.nestedScroll(scrollBehaivor.nestedScrollConnection),
            topBar = {
                TopAppBarFalesia(
                    scrollBehaivor = scrollBehaivor,
                    scope = scope,
                    drawerState = drawerState,
                    titolo = viaDaModificare.id,
                )
            }
        ) {
            val secondPadding = it.calculateTopPadding()
            val columnHeightDp by remember { mutableStateOf(0.dp) }
            val localDensity = LocalDensity.current
            val listSorted = vieNellaFalesia.toList()


            Surface(
                color = Color.Gray,
                modifier = Modifier
                    .padding(
                        top = secondPadding, // + columnHeightDp,
                        start = 8.dp
                    )
                    .wrapContentHeight(),
                shape = RoundedCornerShape(10.dp),
                shadowElevation = 2.dp
            ) {
                Column(
                    //modifier = Modifier.padding(top = secondPadding + columnHeightDp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)

                        ) {


                            EditaDropdown(
                                testo = "falesia",
                                selezioneAttuale = nomeFalesia,
                                selezioneMenu = emptyList()
                            )







                            viaDaModificare.settore =
                                listaNomiSettore?.let { listaNomiSettore ->
                                    EditaDropdown(
                                        testo = "settore",
                                        selezioneAttuale = viaDaModificare.settore,
                                        selezioneMenu = listaNomiSettore
                                    )
                                }.toString()



                            viaDaModificare.viaName =
                                EditaTesto(
                                    "nome",
                                    viaDaModificare.viaName
                                )




                            Row {
                                Column(
                                    modifier = Modifier.weight(1F)
                                ) {
                                    viaDaModificare.grado =
                                        EditaDropdown(
                                            "grado",
                                            viaDaModificare.grado,
                                            listaGradi
                                        )
                                }
                                Column(
                                    modifier = Modifier.weight(1F)
                                ) {


                                    viaDaModificare.altezza =
                                        EditaDropdown(
                                            "altezza",
                                            viaDaModificare.altezza.toString(),
                                            numeri50
                                        ).toInt()
                                }
                                Column(
                                    modifier = Modifier.weight(1F)
                                ) {
                                    Log.d("protezioni", viaDaModificare.protezioni.toString())

                                    viaDaModificare.protezioni =
                                        EditaDropdown(
                                            "protezioni",
                                            viaDaModificare.protezioni.toString(),
                                            numeri50
                                        ).toInt()
                                }

                            }

                            Row {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    TextButton(
                                        onClick = {
                                            Log.d("Falesia id", viaDaModificare.falesiaIdFk)
                                            Log.d("Settore", viaDaModificare.settore)
                                            Log.d("Nome", viaDaModificare.viaName)
                                            Log.d("Grado", viaDaModificare.grado)
                                            Log.d("Altezza", viaDaModificare.altezza.toString())
                                            Log.d(
                                                "Protezioni",
                                                viaDaModificare.protezioni.toString()
                                            )
                                        }

                                    ) {
                                        Text(text = "Aggiorna")
                                    }
                                }

                            }


                        }

                    }

                }

            }

        }


    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditaTesto(
    testo: String,
    selezioneAttuale: String,
    //mutableListOf: MutableList<String>
): String {
    //var ritorno = selezionaAttuale
    //var text by remember { mutableStateOf(TextFieldValue("")) }
    var text by remember { mutableStateOf(selezioneAttuale) }
    if (text == "") {
        text = selezioneAttuale
    }

    Row(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 5.dp),
            fontSize = 12.sp,
            text = testo
        )
    }
    Row(
        modifier = Modifier.padding(start = 12.dp)
    ) {
        //BasicTextField(value = "", onValueChange = {}, Modifier.fillMaxWidth())
        BasicTextField(
            textStyle = TextStyle.Default.copy(fontSize = 30.sp),
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
        )

    }

    Divider(
        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp),
        thickness = 2.dp
    )
    return text

}


@Composable
fun EditaDropdown(
    testo: String,
    selezioneAttuale: String,
    selezioneMenu: List<String>
): String {
    var ritorno = ""
    Row(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 5.dp),
            fontSize = 12.sp,
            text = testo
        )
    }
    Row(
        modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Box(

        ) {
            ritorno =
                dropDownMenu(
                    //secondPadding,
                    30,
                    selezioneAttuale,
                    selezioneMenu
                    //mutableListOf("Settore 1", "Settore 2", "Settore 3")
                )
        }
    }

    Divider(
        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp),
        thickness = 2.dp
    )
    return ritorno
}


@Composable
fun dropDownMenu(
    dimensione: Int,
    selezioneAttuale: String,
    settoriFalesia: List<String>
): String {
    var columnHeightDp by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current
    //val settoriFalesia: MutableList<String> = mutableListOf("Settore 1", "Settore 2", "Settore 3")
    val selezione = rememberSaveable { mutableStateOf(selezioneAttuale) }
    //if (selezione.value == "" || selezione.value == "0"){
    if (selezione.value == "") {
        selezione.value = selezioneAttuale
    }

    val apriMenu = rememberSaveable { mutableStateOf(false) }




    LazyColumn(
    ) {
        item {
            Column(
            ) {


                Text(
                    modifier = Modifier.clickable {
                        apriMenu.value = !apriMenu.value
                    },
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = dimensione.sp,
                    text = selezione.value,
                )

            }
        }

        if (apriMenu.value) {
            settoriFalesia.forEach() { settore ->
                item {
                    Text(
                        modifier = Modifier.clickable {
                            apriMenu.value = !apriMenu.value
                            selezione.value = settore
                        }
                        //.background(color = MaterialTheme.colorScheme.secondaryContainer)
                        ,
                        fontSize = (dimensione * 0.7).sp,
                        text = settore

                    )
                }
            }
        }


    }






    return selezione.value
}


