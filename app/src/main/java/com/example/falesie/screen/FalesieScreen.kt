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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.falesie.Constants
import com.example.falesie.FalesieViewModel
import com.example.falesie.FalesieViewModelFactory
import com.example.falesie.MainActivity.Companion.userCorrente
import com.example.falesie.data.firestore.FirestoreClass
import com.example.falesie.data.firestore.salvaDbFalesieInLocale
import com.example.falesie.data.firestore.salvaDbVieInLocale
import com.example.falesie.data.room.models.Falesia
import com.example.falesie.data.room.models.Via

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FalesieScreen(
    navController: NavHostController,
    factory: FalesieViewModelFactory,
    falesieViewModel : FalesieViewModel = viewModel(factory = factory),
) {

    if (userCorrente.aggiorna) {
    salvaDbVieInLocale(factory)
    salvaDbFalesieInLocale(factory)
        Log.d("Caricamento", "caricamento vie da db online")
//TODO imposta a zero il valore aggiorna dell'userCorrente
        val temp:HashMap<String, Any> = hashMapOf("aggiorna" to false)
        FirestoreClass().updateUserProfileData(
            temp
        )
    }

    val listaVie = falesieViewModel.vieList.collectAsState(initial = emptyList())
    val listaFalesie = falesieViewModel.falesieList.collectAsState(initial = emptyList())
    val scrollBehaivor = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Log.d("TEST admin", userCorrente.admin.toString())
    Log.d("TEST aggiorna", userCorrente.aggiorna.toString())
    Log.d("TEST email", userCorrente.email)



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
                ListaFalesie(paddingValues = it, navController, listaVie, listaFalesie,{
                    //chiamata di ritorno in onSelectFalesia
                    Constants.FALESIACORRENTEID = it.id
                    Log.d("Ritorno della chiamata", it.nome)
                    val route = "${"VieScreen"}/${it.nome}"
                    navController.navigate(route)
                    // esempio navArgument https://www.reddit.com/r/JetpackCompose/comments/15gieu3/how_to_pass_arguments_to_a_composable_using/
                })
            }
        )


    }


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListaFalesie(
    paddingValues: PaddingValues,
    navController: NavHostController,
    listaVie : State<List<Via>>,
    listaFalesie : State<List<Falesia>>,
    onSelectFalesia : (Falesia) -> Unit,
) {
    var listSorted = listaFalesie.value.sortedBy { it.nome }
    Scaffold()
    {
        LazyColumn(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            items(
                //items = listaFalesie.value,
                items = listSorted,
                key = {item -> item.id}
            ){ item ->
                ListItem(falesia = item, { onSelectFalesia(it) })
            }
        }
    }
}









@Composable
fun ListItem(
    falesia: Falesia,
    onSelectFalesia: (Falesia) -> Unit) {
    var pressioneIdFalesia by remember { mutableStateOf("") }
    val expanded = rememberSaveable { mutableStateOf(false) }
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
                                onSelectFalesia(falesia)
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
                }

            }


            if (expanded.value) {
                Column(
                    modifier = Modifier
                        .padding(
                            bottom = extraPadding.coerceAtLeast(0.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
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


}