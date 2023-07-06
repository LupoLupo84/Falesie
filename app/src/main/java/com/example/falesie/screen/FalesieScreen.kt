package com.example.falesie.screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.falesie.Aggiorna
import com.example.falesie.MainActivity
import com.example.falesie.MainActivity.Companion.falesiaSelezionata
import com.example.falesie.MainActivity.Companion.listaFalesie
import com.example.falesie.MainActivity.Companion.listaVie
import com.example.falesie.MainActivity.Companion.listaVieSelezionate
import com.example.falesie.firestore.FirestoreClass
import com.example.falesie.model.Falesia


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FalesieScreen(navController: NavHostController) {
    val scrollBehaivor = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Log.d("TEST", MainActivity.userCorrente.email)


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
                    titolo = "Falesie"
                )
            },
            content = {
                ListaFalesie(paddingValues = it, navController)
            }
        )


    }


}

@Composable
fun ListaFalesie(paddingValues: PaddingValues, navController: NavHostController) {
    var caricamentoCompletato by remember { mutableStateOf(false) }


    if (listaFalesie.size < 1) {
        FirestoreClass().leggiTutteLeFalesie(object : Aggiorna {
            override fun aggiorna() {
                Log.i("Numero di falesie lette nel database", listaFalesie.size.toString())
                FirestoreClass().leggiTutteLeVie(object : Aggiorna {
                    override fun aggiorna() {
                        Log.i("VIE LETTE IF", listaVie.size.toString())
                        caricamentoCompletato = true

                    }
                })


            }
        })
    } else {
        FirestoreClass().leggiTutteLeVie(object : Aggiorna {
            override fun aggiorna() {
                Log.i("VIE LETTE ELSE", listaVie.size.toString())
                caricamentoCompletato = true

            }
        })
    }


    if (caricamentoCompletato == true) {
        //compilaListaFalesie(paddingValues)
        RecyclerView(paddingValues, navController)
    }


}


@Composable
fun RecyclerView(paddingValues: PaddingValues, navController: NavHostController) {

    LazyColumn(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
    ) {

        items(items = listaFalesie.sortedBy { it.nome }) { falesia ->
            ListItem(falesia, navController)
        }

    }

}

//tutorial
//https://www.youtube.com/watch?v=q7qHRnzcfEQ


@Composable
fun ListItem(falesia: Falesia, navController: NavHostController) {
    var pressioneIdFalesia by remember { mutableStateOf("") }
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
                    Text(text = "Falesia")
                    Text(
                        modifier = Modifier
                            .clickable {
                                pressioneIdFalesia = falesia.id
                                falesiaSelezionata = falesia
                                Log.d("FALESIA SELEZIONATA", falesia.id)
                            },
                        text = falesia.nome, style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                }

                OutlinedButton(onClick = { expanded.value = !expanded.value }) {
                    Text(if (expanded.value) "Comprimi" else "Espandi")
                }

            }

            if (expanded.value) {
                Column(
                    modifier = Modifier
                        .padding(
                            bottom = extraPadding.coerceAtLeast(0.dp)
                        )
                ) {
                    if (falesia.altitudine > 0) Text(text = "Altitudine " + falesia.altitudine + "m")
                    if (falesia.latitudine.isNotEmpty()) Text(text = "Lat. " + falesia.latitudine)
                    if (falesia.longitudine.isNotEmpty()) Text(text = "Lon. " + falesia.longitudine)
                    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.size(6.dp))
                    if (falesia.descrizione.isNotEmpty()) Text(text = falesia.descrizione)
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