package com.example.falesie.screen

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.falesie.Constants
import com.example.falesie.FalesieViewModel
import com.example.falesie.FalesieViewModelFactory
import com.example.falesie.data.room.models.Via
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun VieScreen(
    navController: NavHostController,
    nomeFalesia: String,
    factory: FalesieViewModelFactory,
    falesieViewModel: FalesieViewModel = viewModel(factory = factory),
    vieNellaFalesia: State<List<Via>>,
) {
    //val vieNellaFalesia = falesieViewModel.vieNellaFalesia.collectAsState(initial = emptyList())
    Log.d("Vie nella falesia", vieNellaFalesia.value.size.toString())

    val settoriFalesia: MutableList<String> = mutableListOf("Tutti i settori")

    var tempSettoreCorrente = Constants.SETTORECORRENTE

    vieNellaFalesia.value.sortedBy { it.settore }
//       for (i in vieNellaFalesia.value){
//           if (i.settore != Constants.SETTORECORRENTE && !settoriFalesia.contains(i.settore)){
//               Constants.SETTORECORRENTE = i.settore
//               settoriFalesia.add(Constants.SETTORECORRENTE)
//               Log.d("SETTORE", Constants.SETTORECORRENTE)
//           }
//       }
    for (i in vieNellaFalesia.value){
        if (i.settore != tempSettoreCorrente && !settoriFalesia.contains(i.settore)){
            tempSettoreCorrente = i.settore
            settoriFalesia.add(tempSettoreCorrente)
            Log.d("SETTORE", tempSettoreCorrente)
        }
    }
    Log.d("NUMERO DI SETTORI NELLA FALESIA", settoriFalesia.size.toString())



//    val vieNelSettore= mutableListOf<Via>()
//    for (i in vieNellaFalesia.value){
//        if (i.settore != Constants.SETTORECORRENTE){
//            vieNelSettore.add(i)
//        }
//    }




    //Log.d("SETTORI NELLA FALESIA", (settoriFalesia.size-1).toString())
    //Log.d("SETTORE CORRENTE", (settoriFalesia.size-1).toString())

    //val vieNelSettore = vieNellaFalesia.value.filter { it.settore == titoloSettore }
    //val vieNelSettore = vieNellaFalesia.value.filter { it.settore == Constants.SETTORECORRENTE }

    //val vieNelSettore = falesieViewModel.vieNellaFalesiaSettore.collectAsState(initial = emptyList())



    //val vieNelSettore by remember { mutableStateOf(vieNellaFalesia.value.filter { it.settore == titoloSettore })}
//    Log.d("TITOLO SETTORE", titoloSettore)
//    Log.d("VIE NEL SETTORE", vieNelSettore.size.toString())

    val scrollBehaivor = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    //titoloSettore = settoriFalesia[0]           // selezione iniziale del menù
    //Constants.SETTORECORRENTE = settoriFalesia[0]           // selezione iniziale del menù


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
                    titolo = nomeFalesia,
                )
            }
        ) {
            val secondPadding = it.calculateTopPadding()
            var columnHeightDp by remember { mutableStateOf(0.dp) }
            val localDensity = LocalDensity.current

            //titoloSettore = settoriFalesia[0]           // selezione iniziale del menù

            //se i settori sono maggiori di 2 visualizzo il menù di scelta.
            // il primo è sempre "Tutti i settori" mentre il 2 è il settore principale della falesia
            if (settoriFalesia.size > 1) {
                val apriMenuSettori = rememberSaveable { mutableStateOf(false) }
                LazyColumn(
                    modifier = Modifier
                        .padding(top = it.calculateTopPadding())
                        .onGloballyPositioned { coordinates ->
                            // Set column height using the LayoutCoordinates
                            columnHeightDp =
                                with(localDensity) { coordinates.size.height.toDp() }
                        }
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp)
                        ) {
                            TextButton(
                                onClick = {
                                    apriMenuSettori.value = !apriMenuSettori.value
                                }
                            ) {
                                Text(
                                    text = Constants.SETTORECORRENTE,
                                    //text = settoriFalesia[0],
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }

                    if (apriMenuSettori.value) {
                        settoriFalesia.forEach() { settore ->
                            item {
                                TextButton(
                                    modifier = Modifier.padding(start = 16.dp),
                                    onClick = {
                                        Constants.SETTORECORRENTE = settore
                                        //TODO CHIAMA NUOVAMENTE LA FUNZIONE Viescreen
                                        navController.popBackStack()
                                        val route = "${"VieScreen"}/${nomeFalesia}"
                                        navController.navigate(route)

//                                        Log.d("TITOLO SETTORE", Constants.SETTORECORRENTE)
//                                        Log.d("VIE NEL SETTORE", vieNelSettore.value.size.toString())
                                        apriMenuSettori.value = !apriMenuSettori.value
                                    }
                                ) {
                                    Text(text = settore)
                                }
                            }
                        }
                    }


                }
            }

/////////////// inizio della visualizzazione delle vie

            LazyColumn(
                modifier = Modifier
                    .padding(top = secondPadding + columnHeightDp)
            ) {

//ELEMENTI DA VISUALIZZARE NELLA LISTA VIE
                //items(vieNelSettore.value.size) {
                items(vieNellaFalesia.value.size) {

                    //visualizza solamente le vie della falesia corrispondenti al settore corrente oppure se è selezionato il settore tutti i settori
                    if (vieNellaFalesia.value[it].settore == Constants.SETTORECORRENTE || Constants.SETTORECORRENTE == "Tutti i settori"){


//                items(viewModelVia.state.items) {
//                    val viaPresente = it.viaPresente
                    val expanded = rememberSaveable { mutableStateOf(false) }
                    val extraPadding by animateDpAsState(
                        if (expanded.value) 24.dp else 0.dp,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ), label = ""
                    )


                    val bordoOFF =
                        BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.inverseSurface)
                    val stellaOFF = Icons.Default.StarBorder
                    var bordo by remember { mutableStateOf(bordoOFF) }
                    var stella by remember { mutableStateOf(stellaOFF) }
//                    if (viaPresente) {
//                        bordo = BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.primary)
//                        stella = Icons.Default.Star
//                    }


                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                        shape = RoundedCornerShape(10.dp),
                        shadowElevation = 2.dp
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
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Column() {
                                            Text(
                                                //text = "${it.listaVie.numero} - ",
                                                text = "${vieNellaFalesia.value[it].numero} - ",
                                                //text = "${it.numero} - ",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                modifier = Modifier
                                                    .clickable {
                                                        //Log.d("FALESIA SELEZIONATA", it.listaVie.id)
                                                        Log.d(
                                                            "FALESIA SELEZIONATA",
                                                            vieNellaFalesia.value[it].id
                                                        )
                                                    },
                                                //text = it.listaVie.viaName,
                                                text = vieNellaFalesia.value[it].viaName,
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                }

                                OutlinedButton(
                                    border = bordo,
                                    onClick = { expanded.value = !expanded.value }
                                ) {
                                    Text(
                                        //if (expanded.value) "${it.listaVie.grado} ▲" else "${it.listaVie.grado} ▼",
                                        if (expanded.value) "${vieNellaFalesia.value[it].grado} ▲" else "${vieNellaFalesia.value[it].grado} ▼",
                                        color = MaterialTheme.colorScheme.inversePrimary
                                    )
                                }
                            }

                            if (expanded.value) {
                                Column(
                                    modifier = Modifier
                                        .padding(
                                            bottom = extraPadding.coerceAtLeast(0.dp)
                                        )
                                ) {
                                    Row() {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row() {
                                                //if (it.listaVie.altezza == 0) {
                                                if (vieNellaFalesia.value[it].altezza == 0) {
                                                    Text(text = "Altezza ")
                                                    Text(text = "---")
                                                } else {
                                                    Text(text = "Altezza ")
                                                    Text(
                                                        //text = "${it.listaVie.altezza}m",
                                                        text = "${vieNellaFalesia.value[it].altezza}m",
                                                        style = MaterialTheme.typography.titleMedium.copy(
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                        Row() {
                                            //if (it.listaVie.protezioni == 0) {
                                            if (vieNellaFalesia.value[it].protezioni == 0) {
                                                Text(text = "Protezioni ")
                                                Text(text = "---")
                                            } else {
                                                Text(text = "Protezioni ")
                                                Text(
                                                    //text = "${it.listaVie.protezioni}m",
                                                    text = "${vieNellaFalesia.value[it].protezioni}m",
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Icon(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .combinedClickable(
                                                    onClick = {
                                                        Log.d(
                                                            "TEST",
                                                            "CLICK PRESS"
                                                        )
                                                    },
                                                    onLongClick = {
                                                        //TODO controllo se la via non è mai stata scalata
                                                        //FIXME nota di sistemazione
//                                                        if(!it.viaPresente){
// //                                                       if (stella != Icons.Default.Star) {
//                                                            Log.d(
//                                                                "TEST",
//                                                                "LONG CLICK PRESS"
//                                                            )
//                                                            if (calcolaGrado(it.grado) > calcolaGrado(
//                                                                    viewModelVia.userCorrente.gradoMax
//                                                                )
//                                                            ) {
//                                                                viewModelVia.aggiornaProfiloUtente(
//                                                                    it.id,
//                                                                    it.grado
//                                                                )
//                                                            } else {
//                                                                viewModelVia.aggiornaProfiloUtente(
//                                                                    it.id
//                                                                )
//                                                            }
//                                                        }
                                                    }
                                                ),
                                            imageVector = stella,
                                            contentDescription = "stella"
                                        )
                                    }
                                }


                                //se l'utente ha già scalato la via aggiungi il divisore e le ripetizioni

//                                if (viaPresente) {
//                                    Divider(
//                                        thickness = 1.dp,
//                                        color = MaterialTheme.colorScheme.inversePrimary
//                                    )
//                                    Row(
//                                        modifier = Modifier.wrapContentHeight()
//                                    ) {
//                                        Text(text = "Ripetizioni: ")
//                                    }
//
//                                    for (i in 0 until it.ripetizioni.size){
//                                        Row(
//                                            modifier = Modifier.wrapContentHeight()
//                                        ){
//                                            Text(
//                                                fontSize = 11.sp,
//                                                text = it.ripetizioni[i]
//                                            )
//                                        }
//                                    }
//                                }


                            }
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End,
                                    fontSize = 8.sp,
                                    text = "ID: ${vieNellaFalesia.value[it].id}"
                                    //text = "ID: ${it.listaVie.id}"
                                )
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
fun TopAppBarFalesia(
    scrollBehaivor: TopAppBarScrollBehavior,
    scope: CoroutineScope,
    drawerState: DrawerState,
    titolo: String,
) {
    TopAppBar(
        scrollBehavior = scrollBehaivor,
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Icon"
                )
            }
        },
        title = {
            Text(text = titolo)
        },
    )
}
















//
//    val falesia = falesieViewModel
//
//    val viewModelVia = viewModel<VieViewModel>(factory = VieViewModelFactory())
//
//    val titoloSettore = rememberSaveable { mutableStateOf("Tutti i settori") }
//
//    viewModelVia.getVieFalesiaSettore(falesia.id, titoloSettore.value)
//    Log.d("VIE NELLA FALESIA", viewModelVia.state.items.size.toString())
//
//    val arrayDettagliVia = viewModelVia.arrayDettagliVia
//
//    val scrollBehaivor = TopAppBarDefaults.pinnedScrollBehavior()
//    val scope = rememberCoroutineScope()
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//
//
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            ModalDrawerSheetMenu(navController = navController)
//        }
//    ) {
//
//        Scaffold(
//            modifier = Modifier.nestedScroll(scrollBehaivor.nestedScrollConnection),
//            topBar = {
//                TopAppBarFalesia(
//                    scrollBehaivor = scrollBehaivor,
//                    scope = scope,
//                    drawerState = drawerState,
//                    titolo = falesia.nome,
//                )
//            }
//        ) {
//            val secondPadding = it.calculateTopPadding()
//            var columnHeightDp by remember { mutableStateOf(0.dp) }
//            val localDensity = LocalDensity.current
//
//
//            if (viewModelVia.settoriState.size > 1) {
//                val apriMenuSettori = rememberSaveable { mutableStateOf(false) }
//                LazyColumn(
//                    modifier = Modifier
//                        .padding(top = it.calculateTopPadding())
//                        .onGloballyPositioned { coordinates ->
//                            // Set column height using the LayoutCoordinates
//                            columnHeightDp =
//                                with(localDensity) { coordinates.size.height.toDp() }
//                        }
//                ) {
//                    item {
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(start = 16.dp)
//                        ) {
//                            TextButton(
//                                onClick = {
//                                    apriMenuSettori.value = !apriMenuSettori.value
//                                }
//                            ) {
//                                Text(
//                                    text = titoloSettore.value,
//                                    fontSize = 20.sp
//                                )
//                            }
//                        }
//                    }
//
//                    if (apriMenuSettori.value) {
//                        viewModelVia.settoriState.forEach() { settore ->
//                            item {
//                                TextButton(
//                                    onClick = {
//                                        titoloSettore.value = settore
//                                        apriMenuSettori.value = !apriMenuSettori.value
//                                    }
//                                ) {
//                                    Text(text = settore)
//                                }
//                            }
//                        }
//                    }
//
//
//                }
//            }
//
//
//
//            LazyColumn(
//                modifier = Modifier
//                    .padding(top = secondPadding + columnHeightDp)
//            ) {
//
////ELEMENTI DA VISUALIZZARE NELLA LISTA VIE
//                Log.d("ARRAY DETTAGLI VIA", arrayDettagliVia.items.size.toString())
////                Log.d("ARRAY DETTAGLI VIA", viewModelVia.arrayDettagliVia.size.toString())
//                items(arrayDettagliVia.items) {
////                items(viewModelVia.state.items) {
//                    val viaPresente = it.viaPresente
//                    val expanded = rememberSaveable { mutableStateOf(false) }
//                    val extraPadding by animateDpAsState(
//                        if (expanded.value) 24.dp else 0.dp,
//                        animationSpec = spring(
//                            dampingRatio = Spring.DampingRatioMediumBouncy,
//                            stiffness = Spring.StiffnessLow
//                        ), label = ""
//                    )
//
//
//                    val bordoOFF = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.inverseSurface)
//                    val stellaOFF = Icons.Default.StarBorder
//                    var bordo by remember { mutableStateOf(bordoOFF) }
//                    var stella by remember { mutableStateOf(stellaOFF) }
//                    if (viaPresente) {
//                        bordo = BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.primary)
//                        stella = Icons.Default.Star
//                    }
//
//
//                    Surface(
//                        color = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
//                        shape = RoundedCornerShape(10.dp),
//                        shadowElevation = 2.dp
//                    ) {
//                        Column(
//                            modifier = Modifier
//                                .padding(8.dp)
//                                .fillMaxWidth()
//                        ) {
//
//                            Row() {
//                                Column(
//                                    modifier = Modifier
//                                        .weight(1f)
//                                ) {
//                                    Row(verticalAlignment = Alignment.CenterVertically) {
//                                        Column() {
//                                            Text(
//                                                //text = "${it.listaVie.numero} - ",
//                                                text = "${it.numero} - ",
//                                                style = MaterialTheme.typography.titleMedium.copy(
//                                                    fontWeight = FontWeight.Bold
//                                                )
//                                            )
//                                        }
//                                        Column(modifier = Modifier.weight(1f)) {
//                                            Text(
//                                                modifier = Modifier
//                                                    .clickable {
//                                                        //Log.d("FALESIA SELEZIONATA", it.listaVie.id)
//                                                        Log.d("FALESIA SELEZIONATA", it.id)
//                                                    },
//                                                //text = it.listaVie.viaName,
//                                                text = it.viaName,
//                                                style = MaterialTheme.typography.titleLarge.copy(
//                                                    fontWeight = FontWeight.Bold
//                                                )
//                                            )
//                                        }
//                                    }
//                                }
//
//                                OutlinedButton(
//                                    border = bordo,
//                                    onClick = { expanded.value = !expanded.value }
//                                ) {
//                                    Text(
//                                        //if (expanded.value) "${it.listaVie.grado} ▲" else "${it.listaVie.grado} ▼",
//                                        if (expanded.value) "${it.grado} ▲" else "${it.grado} ▼",
//                                        color = MaterialTheme.colorScheme.inversePrimary
//                                    )
//                                }
//                            }
//
//                            if (expanded.value) {
//                                Column(
//                                    modifier = Modifier
//                                        .padding(
//                                            bottom = extraPadding.coerceAtLeast(0.dp)
//                                        )
//                                ) {
//                                    Row() {
//                                        Column(modifier = Modifier.weight(1f)) {
//                                            Row() {
//                                                //if (it.listaVie.altezza == 0) {
//                                                if (it.altezza == 0) {
//                                                    Text(text = "Altezza ")
//                                                    Text(text = "---")
//                                                } else {
//                                                    Text(text = "Altezza ")
//                                                    Text(
//                                                        //text = "${it.listaVie.altezza}m",
//                                                        text = "${it.altezza}m",
//                                                        style = MaterialTheme.typography.titleMedium.copy(
//                                                            fontWeight = FontWeight.Bold
//                                                        )
//                                                    )
//                                                }
//                                            }
//                                        }
//                                        Row() {
//                                            //if (it.listaVie.protezioni == 0) {
//                                            if (it.protezioni == 0) {
//                                                Text(text = "Protezioni ")
//                                                Text(text = "---")
//                                            } else {
//                                                Text(text = "Protezioni ")
//                                                Text(
//                                                    //text = "${it.listaVie.protezioni}m",
//                                                    text = "${it.protezioni}m",
//                                                    style = MaterialTheme.typography.titleMedium.copy(
//                                                        fontWeight = FontWeight.Bold
//                                                    )
//                                                )
//                                            }
//                                        }
//                                    }
//                                    Column(horizontalAlignment = Alignment.End) {
//                                        Icon(
//                                            modifier = Modifier
//                                                .size(48.dp)
//                                                .combinedClickable(
//                                                    onClick = {
//                                                        Log.d(
//                                                            "TEST",
//                                                            "CLICK PRESS"
//                                                        )
//                                                    },
//                                                    onLongClick = {
//                                                        //TODO controllo se la via non è mai stata scalata
//                                                        //FIXME nota di sistemazione
//                                                        if(!it.viaPresente){
// //                                                       if (stella != Icons.Default.Star) {
//                                                            Log.d(
//                                                                "TEST",
//                                                                "LONG CLICK PRESS"
//                                                            )
//                                                            if (calcolaGrado(it.grado) > calcolaGrado(
//                                                                    viewModelVia.userCorrente.gradoMax
//                                                                )
//                                                            ) {
//                                                                viewModelVia.aggiornaProfiloUtente(
//                                                                    it.id,
//                                                                    it.grado
//                                                                )
//                                                            } else {
//                                                                viewModelVia.aggiornaProfiloUtente(
//                                                                    it.id
//                                                                )
//                                                            }
//                                                        }
//                                                    }
//                                                ),
//                                            imageVector = stella,
//                                            contentDescription = "stella"
//                                        )
//                                    }
//                                }
//
//
//                                //se l'utente ha già scalato la via aggiungi il divisore e le ripetizioni
//
//                                if (viaPresente) {
//                                    Divider(
//                                        thickness = 1.dp,
//                                        color = MaterialTheme.colorScheme.inversePrimary
//                                    )
//                                    Row(
//                                        modifier = Modifier.wrapContentHeight()
//                                    ) {
//                                        Text(text = "Ripetizioni: ")
//                                    }
//
//                                    for (i in 0 until it.ripetizioni.size){
//                                        Row(
//                                            modifier = Modifier.wrapContentHeight()
//                                        ){
//                                            Text(
//                                                fontSize = 11.sp,
//                                                text = it.ripetizioni[i]
//                                            )
//                                        }
//                                    }
//                                }
//
//
//                            }
//                            Row(
//                                modifier = Modifier.fillMaxWidth()
//                            ) {
//                                Text(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    textAlign = TextAlign.End,
//                                    fontSize = 8.sp,
//                                    text = "ID: ${it.id}"
//                                    //text = "ID: ${it.listaVie.id}"
//                                )
//                            }
//
//                        }
//
//                    }
//                }
//
//
//            }
//
//
//        }
//
//    }
//
//}

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TopAppBarFalesia(
//    scrollBehaivor: TopAppBarScrollBehavior,
//    scope: CoroutineScope,
//    drawerState: DrawerState,
//    titolo: String,
//) {
//    TopAppBar(
//        scrollBehavior = scrollBehaivor,
//        navigationIcon = {
//            IconButton(onClick = {
//                scope.launch {
//                    drawerState.open()
//                }
//            }) {
//                Icon(
//                    imageVector = Icons.Default.Menu,
//                    contentDescription = "Menu Icon"
//                )
//            }
//        },
//        title = {
//            Text(text = titolo)
//        },
//    )
//}

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DatePickerView() {
//    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
//        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
//            return utcTimeMillis <= System.currentTimeMillis()
//        }
//    })
//    val selectedDate = datePickerState.selectedDateMillis?.let {
//        convertMillisToDate(it)
//    }
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        DatePicker(
//            state = datePickerState
//        )
//        Spacer(
//            modifier = Modifier.height(
//                32.dp
//            )
//        )
//        Button(
//            onClick = {
//                /*TODO*/
//            }) {
//            Text(
//                text = selectedDate.toString(),
//                color = Color.Red
//            )
//        }
//
//    }
//}
//
//private fun convertMillisToDate(millis: Long): String {
//    val formatter = SimpleDateFormat("dd/MM/yyyy")
//    return formatter.format(Date(millis))
//}

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyDatePickerDialog(
//    onDateSelected: (String) -> Unit,
//    onDismiss: () -> Unit
//) {
//    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
//        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
//            return utcTimeMillis <= System.currentTimeMillis()
//        }
//    })
//
//    val selectedDate = datePickerState.selectedDateMillis?.let {
//        convertMillisToDate(it)
//    } ?: ""
//
//    DatePickerDialog(
//        onDismissRequest = { onDismiss() },
//        confirmButton = {
//            Button(onClick = {
//                onDateSelected(selectedDate)
//                onDismiss()
//            }
//
//            ) {
//                Text(text = "OK")
//            }
//        },
//        dismissButton = {
//            Button(onClick = {
//                onDismiss()
//            }) {
//                Text(text = "Cancel")
//            }
//        }
//    ) {
//        DatePicker(
//            state = datePickerState
//        )
//    }
//}
//

//@Composable
//fun MyDatePickerDialog(): String {
//    var date by remember {
//        mutableStateOf("Open date picker dialog")
//    }
//
//    var showDatePicker by remember {
//        mutableStateOf(true)
//    }
//
////    Box(contentAlignment = Alignment.Center) {
////        Button(onClick = { showDatePicker = true }) {
////            Text(text = date)
////        }
////    }
//
//    if (showDatePicker) {
//        MyDatePickerDialog(
//            onDateSelected = { date = it },
//            onDismiss = {
//                showDatePicker = false
//                date = ""
//            }
//        )
//    }
//    return date
//}




