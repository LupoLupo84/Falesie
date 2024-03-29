package com.example.falesie.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
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
    val listaFalesie = falesieViewModel.falesieList.collectAsState(initial = emptyList())


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheetMenu(navController = navController)
        }
    ) {

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
                GestioneFalesieFrame(paddingValues = it, navController, listaFalesie)

            }
        )


    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GestioneFalesieFrame(
    paddingValues: PaddingValues,
    navController: NavHostController,
    listaFalesie: State<List<Falesia>>
) {
    val listSorted = listaFalesie.value.sortedBy { it.nome }
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
                    Constants.FALESIACORRENTEID = it.id
                    val route = "${"ModificaVieScreen"}/${it.nome}"
                    navController.navigate(route)
                })
            }
        }
    }
}


@Composable
fun VisualizzaFalesie(
    falesia: Falesia,
    onSelectFalesia: (Falesia) -> Unit
) {


    Surface(
        //color = MaterialTheme.colorScheme.primary,
        color = Color.Gray,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 2.dp
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                Text(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 3.dp, bottom = 3.dp),
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