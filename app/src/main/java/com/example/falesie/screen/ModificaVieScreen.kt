package com.example.falesie.screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.falesie.Constants
import com.example.falesie.FalesieViewModel
import com.example.falesie.FalesieViewModelFactory
import com.example.falesie.data.room.models.Via


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ModificaVieScreen(
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

    for (i in vieNellaFalesia.value) {
        if (i.settore != tempSettoreCorrente && !settoriFalesia.contains(i.settore)) {
            tempSettoreCorrente = i.settore
            settoriFalesia.add(tempSettoreCorrente)
            Log.d("SETTORE", tempSettoreCorrente)
        }
    }
    Log.d("NUMERO DI SETTORI NELLA FALESIA", settoriFalesia.size.toString())


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
                    titolo = nomeFalesia,
                )
            }
        ) {
            val secondPadding = it.calculateTopPadding()
            var columnHeightDp by remember { mutableStateOf(0.dp) }
            val localDensity = LocalDensity.current

            val listSorted = vieNellaFalesia.value.toList()



            LazyColumn(
                modifier = Modifier
                    .padding(top = secondPadding + columnHeightDp)
            ) {

                items(
                    items = listSorted,
                    key = { item -> item.id }
                ) { item ->

                    VisualizzaVie(via = item, {
                        //GESTIONE onSelectVia
                        //Constants.FALESIACORRENTEID = it.id
                        //val route = "${"ModificaVieScreen"}/${it.nome}"
                        //navController.navigate(route)
                    })

                }

            }


        }


    }

}


@Composable
fun VisualizzaVie(
    via: Via,
    onSelectVia: (Via) -> Unit
) {


    Surface(
        //color = MaterialTheme.colorScheme.primary,
        color = Color.Gray,
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
                    Row {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Row {
                                Text(text = via.settore)
                            }
                            Row {
                                Text(
                                    text = "${via.numero} - ${via.viaName}",
                                )
                            }
                        }
                        Column(
                        ) {
                            IconButton(onClick = {
                                onSelectVia(via)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = ""
                                )
                            }
                        }
                    }


                    Row {

                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(text = "grado ${via.grado}")
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(text = "altezza ${via.altezza} m")
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(text = "protezioni ${via.protezioni}")
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    fontSize = 8.sp,
                    text = "ID: ${via.id}"
                )
            }

        }
    }
}




