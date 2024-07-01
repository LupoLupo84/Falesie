package com.example.falesie.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsApplications
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.falesie.Constants
import com.example.falesie.FalesieViewModel
import com.example.falesie.FalesieViewModelFactory
import com.example.falesie.data.room.models.Falesia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestioneFalesieScreen(
    navController: NavHostController,
    factory: FalesieViewModelFactory,
    falesieViewModel: FalesieViewModel = viewModel(factory = factory),
) {

    val scrollBehaivor = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    falesieViewModel.getFalesieList()
    val listaFalesie by falesieViewModel.falesieList.observeAsState()



    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheetMenu(navController = navController)
        }
    ) {

        listaFalesie?.let { it2 ->

            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehaivor.nestedScrollConnection),
                topBar = {
                    topAppBarCustom(
                        scrollBehaivor = scrollBehaivor,
                        scope = scope,
                        drawerState = drawerState,
                        titolo = "Gestione Falesie"
                    )
                },
                content = {
                    //CustomList(paddingValues = it)
                    GestioneFalesieFrame(paddingValues = it, navController, it2)

                }
            )
        }


    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GestioneFalesieFrame(
    paddingValues: PaddingValues,
    navController: NavHostController,
    //listaFalesie: State<List<Falesia>>
    listaFalesie: List<Falesia>
) {
    val listSorted = listaFalesie.sortedBy { it.nome }
    Scaffold()
    {
        LazyColumn(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            items(
                items = listSorted,
                key = { item -> item.id }
            ) { item ->

                // RITORNO DELLA CHIAMATA SULLA FUNZIONE ONCLICK DELLA FALESIA
                VisualizzaFalesie(falesia = item, {
                    val route = "${"ModificaVieScreen"}/${it.nome}"
                    Constants.FALESIACORRENTEID = it.id
                    navController.navigate(route)
                },
                {
                    val route2 = "${"ModificaFalesiaScreen"}/${it.nome}"
                    Constants.FALESIACORRENTEID = it.id
                    navController.navigate(route2)
                }
                )
            }
        }
    }
}


@Composable
fun VisualizzaFalesie(
    falesia: Falesia,
    onSelectFalesia: (Falesia) -> Unit,
    onSelectFalesia2: (Falesia) -> Unit
) {


    Surface(
        //color = MaterialTheme.colorScheme.primary,
        color = Color.Gray,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 2.dp
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
            ) {
                IconButton(onClick = {
                    onSelectFalesia2(falesia)
                    Log.d("FALESIA SELEZIONATA", falesia.id)
                }) {
                    Icon(imageVector = Icons.Filled.SettingsApplications, contentDescription = "")
                }

            }

            Column(
                modifier = Modifier
                    //.padding(8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                    Text(
//                        modifier = Modifier
//                            .padding(start = 8.dp, top = 3.dp, bottom = 3.dp),
                        fontSize = 24.sp,
                        text = falesia.nome
                    )

            }
            Column(
            ) {
                IconButton(onClick = {
                    onSelectFalesia(falesia)
                    Log.d("FALESIA SELEZIONATA", falesia.id)
                }) {
                    Icon(imageVector = Icons.Filled.Settings, contentDescription = "")
                }

            }
        }
    }
}