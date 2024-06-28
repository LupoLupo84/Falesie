package com.example.falesie.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Divider
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
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
    falesieViewModel: FalesieViewModel = viewModel(factory = factory),
) {
    val listaVie by falesieViewModel.vieList.observeAsState()
    val listaFalesie by falesieViewModel.falesieList.observeAsState()
    val context = LocalContext.current

    if (userCorrente.aggiorna) {
        salvaDbVieInLocale(factory)
        salvaDbFalesieInLocale(factory)
        Log.d("Caricamento", "caricamento vie da db online")
//TODO imposta a zero il valore aggiorna dell'userCorrente
        val resetAggiorna: HashMap<String, Any> = hashMapOf("aggiorna" to false)
        FirestoreClass().updateUserProfileData(
            resetAggiorna
        )
        Log.d("Reset", "AGGIORNA")
        userCorrente = FirestoreClass().firstLoadUserData()


    }


    falesieViewModel.getVieList()
    falesieViewModel.getFalesieList()
    Log.d("ListavieLocale", listaVie?.size.toString())






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
        listaFalesie?.let {

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

                    ListaFalesie(paddingValues = it, navController, listaVie, listaFalesie) {
                        //chiamata di ritorno in onSelectFalesia
                        Constants.FALESIACORRENTEID = it.id
                        Log.d("Ritorno della chiamata", it.nome)
                        val route = "${"VieScreen"}/${it.nome}"
                        navController.navigate(route)
                        // esempio navArgument https://www.reddit.com/r/JetpackCompose/comments/15gieu3/how_to_pass_arguments_to_a_composable_using/


                    }
                }
            )


        }


    }


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListaFalesie(
    paddingValues: PaddingValues,
    navController: NavHostController,
    listaVie: List<Via>?,
    listaFalesie: List<Falesia>?,
    onSelectFalesia: (Falesia) -> Unit,
) {
    val listSorted = listaFalesie?.sortedBy { it.nome }
    Scaffold()
    {
        LazyColumn(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            items(
                items = listSorted!!,
                key = { item -> item.id }
            ) { item ->
                ListItem(
                    falesia = item, { onSelectFalesia(it) },
                    listaVie)
            }
        }
    }
}


@Composable
fun ListItem(
    falesia: Falesia,
    onSelectFalesia: (Falesia) -> Unit,
    listaVie: List<Via>?
) {
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
                            fontWeight = FontWeight.Bold,
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(2f,2f),
                                blurRadius = 2f
                            )
                        ),


                    )

                }

                OutlinedButton(onClick = { expanded.value = !expanded.value }) {
                    Text(
                        if (expanded.value) "Comprimi ▲" else "Espandi ▼",
                        color = MaterialTheme.colorScheme.inversePrimary,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(1f,1f),
                                blurRadius = 1f
                            )
                        )
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val context = LocalContext.current
                            if (falesia.latitudine.isNotEmpty() && falesia.longitudine.isNotEmpty()) {
                                IconButton(onClick = {
                                    val gmmIntentUri = Uri.parse("geo:0,0?q=${falesia.latitudine},${falesia.longitudine}")
                                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                    mapIntent.setPackage("com.google.android.apps.maps")
                                    startActivity(context, mapIntent, null)
                                }
                                ){
                                    Icon(
                                        modifier = Modifier.size(48.dp),
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                    )
                                }
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
                                            fontWeight = FontWeight.Bold
                                        )
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

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //QUI VA IL GRAFICO A BARRE DELLE VIE PRESENTI NELLA FALESIA
                    DisegnaGrafico(falesia, listaVie)
                }

            }

        }
    }


}




@Composable
fun DisegnaGrafico(falesia: Falesia, listaVie: List<Via>?) {

    Log.d("ListaVie", listaVie?.size.toString())
    val listaVieFalesia = listaVie?.filter { it.falesiaIdFk == falesia.id }

    val lista3 = mutableListOf<String>()
    val lista4 = mutableListOf<String>()
    val lista5 = mutableListOf<String>()
    val lista6 = mutableListOf<String>()
    val lista7 = mutableListOf<String>()
    val lista8 = mutableListOf<String>()
    val lista9 = mutableListOf<String>()
    if (listaVieFalesia != null) {
        for (i in listaVieFalesia) {
            if (i.grado.startsWith("3")) lista3.add(i.viaName)
            if (i.grado.startsWith("4")) lista4.add(i.viaName)
            if (i.grado.startsWith("5")) lista5.add(i.viaName)
            if (i.grado.startsWith("6")) lista6.add(i.viaName)
            if (i.grado.startsWith("7")) lista7.add(i.viaName)
            if (i.grado.startsWith("8")) lista8.add(i.viaName)
            if (i.grado.startsWith("9")) lista9.add(i.viaName)
        }
    }
    //val hexColor = hexToColor("#54FF00")
    val barData = listOf(
        BarData("3a/3c+", lista3.size.toFloat(), hexToColor("#54FF00")),
        BarData("4a/4c+", lista4.size.toFloat(), hexToColor("#8CFF00")),
        BarData("5a/5c+", lista5.size.toFloat(), hexToColor("#C4FF00")),
        BarData("6a/6c+", lista6.size.toFloat(), hexToColor("#FFFF00")),
        BarData("7a/7c+", lista7.size.toFloat(), hexToColor("#FFC400")),
        BarData("8a/8c+", lista8.size.toFloat(), hexToColor("#FF8C00")),
        BarData("9a/9c+", lista9.size.toFloat(), hexToColor("#FF5400")),
    )

    HorizontalBarChart(barData)
}





@Composable
fun HorizontalBarChart(
    barData: List<BarData>,
    barHeight: Dp = 30.dp,
    spacing: Dp = 16.dp
) {
    val maxValue = barData.maxOfOrNull { it.value } ?: 0f


    Column(
        //modifier = Modifier.padding(16.dp)
    ) {
        barData.forEach { data ->
            var columnWidthDp by remember { mutableStateOf(0.dp) }
            val lunghezzaColonna = (columnWidthDp.value*data.value)/maxValue
            val densità = LocalDensity.current

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacing)
                .onGloballyPositioned { coordinates ->
                    columnWidthDp = with(densità) { coordinates.size.width.toDp() }
                }
                ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (data.value > 0) {
                    Text(
                        text = data.label,
                        modifier = Modifier.wrapContentWidth(), // Adjust label width as needed
                                style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(2f,2f),
                                    blurRadius = 2f
                                )
                                ),
                        color = data.color,
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Box(
                        modifier = Modifier
                            .height(barHeight)
                            .weight(1f) // Fill available space
                    ) {
                        Box(
                            modifier = Modifier
                                .height(barHeight)
                                .width(lunghezzaColonna.dp)
                                .background(data.color)
                                .align(Alignment.CenterStart)
                                .border(1.dp, Color.Black)
                                //.clickable{ Toast.makeText(this, "Vie contenute in ${data.label}", Toast.LENGTH_SHORT).show() }
                        )




                    }
                    Text(
                        modifier = Modifier.wrapContentWidth()
                        .padding(start = 3.dp),
                        text = data.value.toInt().toString(),
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(2f,2f),
                                blurRadius = 2f
                            )
                        ),
                        color = data.color,
                    )
                }
            }
        }
    }
}

data class BarData(
    val label: String,
    val value: Float,
    val color: Color
)

fun hexToColor(hex: String): Color {
    return Color(android.graphics.Color.parseColor(hex))
}









