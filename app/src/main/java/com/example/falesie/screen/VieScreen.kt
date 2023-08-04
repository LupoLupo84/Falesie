package com.example.falesie.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.falesie.MainActivity.Companion.arrayVieScalateUser
import com.example.falesie.MainActivity.Companion.userCorrente
import com.example.falesie.data.room.models.Falesia
import com.example.falesie.data.room.models.Via
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VieScreen(navController: NavHostController, falesia: Falesia) {

    val viewModelVia = viewModel<FalesieViewModel>(factory = FalesieViewModelFactory())
    val vieState = viewModelVia.state

    var vieNellaFalesia: MutableList<Via> = ArrayList()

    for (i in vieState.items) {
        if (i.falesiaIdFk == falesia.id) {
            vieNellaFalesia.add(i)
        }
    }

    vieNellaFalesia.sortBy { it.numero }





    for (i in userCorrente.vieScalate) {
        arrayVieScalateUser.add(i)
    }

    val scrollBehaivor = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheetMenu(navController = navController, null)
        }
    ) {

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehaivor.nestedScrollConnection),
            topBar = {
                topAppBarFalesia(
                    scrollBehaivor = scrollBehaivor,
                    scope = scope,
                    drawerState = drawerState,
                    //titolo = "Vie"
                    titolo = falesia.nome,
                    vieNellaFalesia
                )
            },
            content = {
                //CustomList(paddingValues = it)
                ListaVie(paddingValues = it, navController, falesia, vieNellaFalesia)

            }
        )


    }

}


@Composable
fun ListaVie(
    paddingValues: PaddingValues,
    navController: NavHostController,
    falesia: Falesia,
    vieNellaFalesia: MutableList<Via>
) {
//    val viewModelVia = viewModel<FalesieViewModel>(factory = FalesieViewModelFactory())
//    val vieState = viewModelVia.state
//
//    var vieNellaFalesia: MutableList<Via> = ArrayList()
//
//    for (i in vieState.items) {
//        if (i.falesiaIdFk == falesia.id) {
//            vieNellaFalesia.add(i)
//        }
//    }
//
//    vieNellaFalesia.sortBy { it.numero }


    LazyColumn(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
    ) {


        items(items = vieNellaFalesia.sortedBy { it.numero }) { via ->
            ListItem(via, navController)
        }


    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItem(via: Via, navController: NavHostController) {
    //var pressioneIdVia by remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    val extraPadding by animateDpAsState(
        if (expanded.value) 24.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )




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


            var viaPresente = false

            val bordoON =
                BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.inversePrimary)
            val bordoOFF =
                BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.inverseSurface)
            //var bordo = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.secondary)
            var bordo by remember { mutableStateOf(bordoOFF) }
            //var stella = Icons.Default.StarBorder
            val stellaON = Icons.Default.Star
            val stellaOFF = Icons.Default.StarBorder
            var stella by remember { mutableStateOf(stellaOFF) }
            var arrayDateScalata: ArrayList<String> = ArrayList()

            for (i in arrayVieScalateUser) {

                if (i.id == via.id) {
                    arrayDateScalata = i.dataRipetizioni as ArrayList
                    Log.d("TEST", "VIA PRESENTE")
                    viaPresente = true
                    bordo = BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.primary)
                    stella = Icons.Default.Star
                }
            }





            Row() {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        fontSize = 8.sp,
                        text = "ID: ${via.id}"
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column() {
                            Text(
                                text = "${via.numero} - ",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                modifier = Modifier
                                    .clickable {
                                        //viaSelezionata = via
                                        Log.d("FALESIA SELEZIONATA", via.id)
                                    },
                                text = via.viaName,
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
                        if (expanded.value) "${via.grado} ▲" else "${via.grado} ▼",
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
                                if (via.altezza == 0) {
                                    Text(text = "Altezza ")
                                    Text(text = "---")
                                } else {
                                    Text(text = "Altezza ")
                                    Text(
                                        text = "${via.altezza}m",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                            Row() {
//                                if (via.protezioni > 0) {
                                Text(text = "Protezioni ")
                                Text(
                                    text = "${via.protezioni}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
//                                }
                            }


                        }
                        Column(horizontalAlignment = Alignment.End) {
//                            IconButton(
//                                modifier = Modifier.combinedClickable (
//                                    onClick = { Log.d("TEST", "CLICK PRESS") },
//                                    onLongClick = {
//                                        Log.d("TEST", "LONG CLICK PRESS")
//                                        stella = Icons.Default.Star
//                                        //onLongClickStart(via = via)
//                                    }
//                                        ),
//                                onClick = { /*TODO*/ },
//                            ) {
//                                Icon(
//                                    modifier = Modifier.size(48.dp),
//                                    imageVector = stella,
//                                    contentDescription = "stella"
//                                )
//                            }

                            Icon(
                                modifier = Modifier
                                    .size(48.dp)
                                    .combinedClickable(
                                        onClick = { Log.d("TEST", "CLICK PRESS") },
                                        onLongClick = {
                                            Log.d("TEST", "LONG CLICK PRESS")
                                            if (stella == stellaOFF) {
                                                stella = stellaON
                                                bordo = bordoON
                                            } else {
                                                stella = stellaOFF
                                                bordo = bordoOFF
                                            }
                                            //onLongClickIcon(via = via)
                                        }
                                    ),
                                imageVector = stella,
                                contentDescription = "stella"
                            )
                        }

                    }


                    //TODO se l'utente ha già scalato la via aggiungi il divisore e le ripetizioni

                    if (viaPresente) {
                        Divider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(
                            modifier = Modifier.wrapContentHeight()
                        ) {
                            Text(text = "Ripetizioni: ")
                        }
                        for (i in arrayDateScalata) {
                            Row() {
                                Text(
                                    fontSize = 10.sp,
                                    text = i
                                )
                            }
                        }

                        viaPresente = false
                    }


                }

            }

        }
    }


}


fun onLongClickIcon(via: Via) {
    //Aggiungi la via alle vie scalate user
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBarFalesia(
    scrollBehaivor: TopAppBarScrollBehavior,
    scope: CoroutineScope,
    drawerState: DrawerState,
    titolo: String,
    vieNellaFalesia: MutableList<Via>
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
        actions = { SelezionaSettori(vieNellaFalesia) }

    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelezionaSettori(vieNellaFalesia: MutableList<Via>) {
    val expandedSettori = remember { mutableStateOf(false) }
    val extraPaddingSettori by animateDpAsState(
        if (expandedSettori.value) 24.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )


    IconButton(
        modifier = Modifier.padding(start = 45.dp),
        onClick = {
            expandedSettori.value = !expandedSettori.value
            Log.d("MENU_ITEM", "Click su Seleziona Settori ${expandedSettori.value}")
        }
    ) {
        Icon(
            imageVector = Icons.Default.Abc,
            contentDescription = "Scegli settore"
        )
    }
    if (expandedSettori.value) {

        var settori: ArrayList<String> = arrayListOf()

        vieNellaFalesia.sortBy { it.settore }
        var settore = ""
        for (i in vieNellaFalesia){
            if (i.settore != settore){
                settore = i.settore
                settori.add(i.settore)
            }
        }

        //Demo_ExposedDropdownMenuBox()
        val context = LocalContext.current
        //val settori = arrayOf("Americano", "Cappuccino", "Espresso", "Latte", "Mocha")
        var expanded by remember { mutableStateOf(false) }
        var selectedText by remember { mutableStateOf(settori[0]) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
            //.padding(32.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    settori.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedText = item
                                expanded = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                expandedSettori.value = !expandedSettori.value

                                // ritorna alla funzione chiamante il filtro per visualizzare
                                // solo le vie nella falesia selezionata

                            }
                        )
                    }
                }
            }
        }
    }

}

