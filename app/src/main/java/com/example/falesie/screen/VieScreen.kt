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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.falesie.MainActivity.Companion.arrayVieScalateUser
import com.example.falesie.MainActivity.Companion.falesiaSelezionata
import com.example.falesie.MainActivity.Companion.listaVie
import com.example.falesie.MainActivity.Companion.listaVieSelezionate
import com.example.falesie.MainActivity.Companion.userCorrente
import com.example.falesie.MainActivity.Companion.viaSelezionata
import com.example.falesie.model.Via

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VieScreen(navController: NavHostController) {
    for (i in userCorrente.vieScalate) {
        arrayVieScalateUser.add(i)
    }

    val scrollBehaivor = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            modalDrawerSheetMenu(navController = navController)
        }
    ) {

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehaivor.nestedScrollConnection),
            topBar = {
                topAppBarCustom(
                    scrollBehaivor = scrollBehaivor,
                    scope = scope,
                    drawerState = drawerState,
                    //titolo = "Vie"
                    titolo = falesiaSelezionata.nome
                )
            },
            content = {
                //CustomList(paddingValues = it)
                ListaVie(paddingValues = it, navController)

            }
        )


    }

}


@Composable
fun ListaVie(paddingValues: PaddingValues, navController: NavHostController) {

    //Log.d("TEST VIE", listaVie.size.toString())
    // cerca in listaVie le vie facente parte della falesia
    listaVieSelezionate =
        ArrayList()       // svuota l'array delle vie presenti nella falesia corrente
    for (i in listaVie) {
        if (i.falesia == falesiaSelezionata.id) {
            listaVieSelezionate.add(i)
        }
    }
    listaVieSelezionate.sortBy { list -> list.numero }

    //var listaSettore: ArrayList<Via> = ArrayList()
    //var settore = ""


    LazyColumn(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
    ) {


        items(items = listaVieSelezionate.sortedBy { it.numero }) { via ->
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
        color = MaterialTheme.colorScheme.inversePrimary,
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

            val bordoON = BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.primary)
            val bordoOFF = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.secondary)
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
                                        viaSelezionata = via
                                        Log.d("FALESIA SELEZIONATA", via.id)
                                    },
                                text = via.nome,
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
                    Text(if (expanded.value) "${via.grado} ▲" else "${via.grado} ▼")
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
                                var altezza =via.altezza
                                if (via.altezza == 0) {
                                    Text(text = "Altezza ")
                                    Text(text = "---")
                                }else{
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
                                                }else{
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


fun onLongClickIcon(via: Via,){
    //Aggiungi la via alle vie scalate user
}