package com.example.falesie.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.falesie.Aggiorna
import com.example.falesie.Graph.repository
import com.example.falesie.MainActivity.Companion.listaFalesie
import com.example.falesie.MainActivity.Companion.listaVie
import com.example.falesie.MainActivity.Companion.userCorrente
import com.example.falesie.data.room.models.Falesia
import com.example.falesie.data.room.models.Via
import com.example.falesie.firestore.FirestoreClass
import com.example.falesie.room.ViarEvent
import kotlinx.coroutines.runBlocking


var falesiaSelected = Falesia(
    "",
    "",
    "",
    "",
    "",
    0,
    0,
    false,
    false,
    false,
    false,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FalesieScreen(
    navController: NavHostController,
    onEvent: (ViarEvent) -> Unit
) {


    //val viewModel = viewModel<FalesieViewModel>(factory = FalesieViewModelFactory())
    //Log.d("TEST size storelistvie", viewModel.state.storeListVie.size.toString())

    val scrollBehaivor = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Log.d("TEST", userCorrente.email)


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheetMenu(navController = navController, onEvent)
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehaivor.nestedScrollConnection),
            topBar = {
                topAppBarCustom(
                    scrollBehaivor = scrollBehaivor,
                    scope = scope,
                    drawerState = drawerState,
                    titolo = "Falesie"
                )
            },
            content = {
                ListaFalesie(paddingValues = it, navController)
            }
        )


    }


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListaFalesie(
    paddingValues: PaddingValues,
    navController: NavHostController,
) {

    //salvaDbVieInLocale()
    //salvaDbFalesieInLocale()


    val viewModelVia = viewModel<FalesieViewModel>(factory = FalesieViewModelFactory())
    val vieState = viewModelVia.state
    val falesieState = viewModelVia.stateFalesie

    var listaTempFalesie: MutableList<Falesia> = ArrayList()

    for (i in falesieState.items){
        listaTempFalesie.add(i)
    }

    listaTempFalesie.sortBy { it.nome }




    Scaffold()
    {
        LazyColumn(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
        ) {

            items(listaTempFalesie) { falesia ->
                ListItem(falesia, navController)
            }




        }


    }


}


@Composable
fun salvaDbVieInLocale() {
    //USATA PER SALVARE IL DATABASE DI RETE NEL DATABASE LOCALE
    var caricamentoCompletato by remember { mutableStateOf(false) }
    FirestoreClass().leggiTutteLeVie(object : Aggiorna {
        override fun aggiorna() {
            Log.i("VIE LETTE ELSE", listaVie.size.toString())
            caricamentoCompletato = true
        }
    })
    if (caricamentoCompletato == true) {
        //compilaListaFalesie(paddingValues)
        //RecyclerView(paddingValues, navController)
        //SALVA LE VIE NEL DATABASE LOCALE ROOM
        salvaVieInLocale()
    }
}

@Composable
fun salvaDbFalesieInLocale() {
    //USATA PER SALVARE IL DATABASE DI RETE NEL DATABASE LOCALE
    var caricamentoCompletato by remember { mutableStateOf(false) }
    FirestoreClass().leggiTutteLeFalesie(object : Aggiorna {
        override fun aggiorna() {
            Log.i("FALESIE LETTE ELSE", listaFalesie.size.toString())
            caricamentoCompletato = true
        }
    })
    if (caricamentoCompletato == true) {

        //SALVA LE VIE NEL DATABASE LOCALE ROOM
        salvaFalesieInLocale()
    }
}


fun salvaVieInLocale() {
    for (i in listaVie) {
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
        //repository.insertVia(viaRoom)
        runBlocking { repository.insertVia(viaRoom) }
    }
}

fun salvaFalesieInLocale() {
    for (i in listaFalesie) {
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
        //repository.insertVia(viaRoom)
        runBlocking { repository.insertFalesia(falesiaRoom) }
    }
}







@Composable
fun ListItem(falesia: com.example.falesie.data.room.models.Falesia, navController: NavHostController) {
    var pressioneIdFalesia by remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    val extraPadding by animateDpAsState(
        if (expanded.value) 24.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = ""
    )

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 5.dp
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {

            Row() {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                pressioneIdFalesia = falesia.id
                                //falesiaSelezionata = falesia
                                falesiaSelected = falesia
                                Log.d("FALESIA SELEZIONATA", falesia.id)
                            },
                        text = falesia.nome,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                }

                OutlinedButton(onClick = { expanded.value = !expanded.value }) {
                    Text(if (expanded.value) "Comprimi ▲" else "Espandi ▼", color = MaterialTheme.colorScheme.inversePrimary)

                    //if (expanded.value) "${via.grado} ▲" else "${via.grado} ▼",
                }

            }


            if (expanded.value) {
                Column(
                    modifier = Modifier
                        .padding(
                            bottom = extraPadding.coerceAtLeast(0.dp)
                        )
                ) {
                    //if (falesia.altitudine > 0) Text(text = "Altitudine " + falesia.altitudine + "m")
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .wrapContentHeight()
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (falesia.latitudine.isNotEmpty() && falesia.longitudine.isNotEmpty()) {
                                Icon(
                                    modifier = Modifier.size(48.dp),
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null
                                )
                                Column(
                                    modifier = Modifier.padding(start = 6.dp)
                                ) {
                                    Text(
                                        text = "Lat. " + falesia.latitudine,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "Lon. " + falesia.longitudine,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                                Column(
                                ) {
                                    if (falesia.altitudine > 0) {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.End,
                                            text = "Altitudine " + falesia.altitudine + "m",
                                            fontSize = 18.sp,
                                            style = MaterialTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.Bold)
                                        )
                                    }
                                }
                            }

                    }
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.inversePrimary)
                    Spacer(modifier = Modifier.size(6.dp))
                    if (falesia.descrizione.isNotEmpty()) Text(text = falesia.descrizione)
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        text = "Id: ${falesia.id}",
                        fontSize = 8.sp
                    )
                }

            }

        }
    }

    if (pressioneIdFalesia.isNotEmpty()) {       //falesiaSelezionata id falesia


//        when (pressioneIdFalesia) {
//
//            falesiaSelezionata.id -> {
//                //Log.d("SELEZIONE", selectedItems)
                pressioneIdFalesia = ""
//                //navController.popBackStack()
                navController.navigate("VieScreen")
//            }
//        }
    }


}