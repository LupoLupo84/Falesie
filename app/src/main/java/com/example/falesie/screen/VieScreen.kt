package com.example.falesie.screen

import android.os.Build
import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
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
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.falesie.MainActivity.Companion.arrayVieScalateUser
import com.example.falesie.MainActivity.Companion.userCorrente
import com.example.falesie.data.room.models.Falesia
import com.example.falesie.data.room.models.Via
import com.example.falesie.model.ViaScalata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.falesie.MainActivity
import com.example.falesie.firestore.FirestoreClass
import com.example.falesie.tool.calcolaGrado
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.Year
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.HashMap
import java.util.TimeZone



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VieScreen(navController: NavHostController, falesia: Falesia) {


    val viewModelVia = viewModel<VieViewModel>(factory = VieViewModelFactory())

    val titoloSettore = remember { mutableStateOf("Tutti i settori") }

    viewModelVia.getVieFalesiaSettore(falesia.id,titoloSettore.value)
    //val vieNellaFalesia = viewModelVia.state
    Log.d("VIE NELLA FALESIA", viewModelVia.state.items.size.toString())

    //var arrayVieScalateUser by remember { mutableListOf(ArrayList<ViaScalata>) }

    //val arrayVieScalateUser: MutableList<ViaScalata> = arrayListOf()


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
                TopAppBarFalesia(
                    scrollBehaivor = scrollBehaivor,
                    scope = scope,
                    drawerState = drawerState,
                    //titolo = "Vie"
                    titolo = falesia.nome,
                    //vieNellaFalesia
                )


            }
        ) {
            val secondPadding = it.calculateTopPadding()
            var columnHeightDp by remember { mutableStateOf(0.dp) }
            val localDensity = LocalDensity.current


            if (viewModelVia.settoriState.size > 1) {
                val apriMenuSettori = remember { mutableStateOf(false) }
                LazyColumn(
                    modifier = Modifier
                        .padding(top = it.calculateTopPadding())
                        .onGloballyPositioned { coordinates ->
                            // Set column height using the LayoutCoordinates
                            columnHeightDp = with(localDensity) { coordinates.size.height.toDp() }
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
                                    text = titoloSettore.value,
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }

                    if (apriMenuSettori.value) {
                        viewModelVia.settoriState.forEach() { settore ->
                        //settori.forEach() { settore ->
                            item {
                                TextButton(
                                    onClick = {
                                        titoloSettore.value = settore
                                        //viewModelVia.getVieFalesiaSettore(falesia.id,titoloSettore.value)
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



            LazyColumn(
                modifier = Modifier
                    .padding(top = secondPadding + columnHeightDp)
            ) {


                items(items = viewModelVia.state.items){via ->
                    ListItem(via, navController)
                }





            }












        }


    }

}







//@Composable
//fun ListaVie(
//    paddingValues: PaddingValues,
//    navController: NavHostController,
////    vieNellaFalesia: MutableList<Via>
//    idFalesia: String
//) {
//
////    val viewModelVia = viewModel<VieViewModel>(factory = VieViewModelFactory())
////    val vieState = viewModelVia.state
//
//    val viewModelVia = viewModel<VieViewModel>(factory = VieViewModelFactory())
//    val secondPadding = paddingValues.calculateTopPadding()
//    var columnHeightDp by remember { mutableStateOf(0.dp) }
//    val localDensity = LocalDensity.current
//    val titoloSettore = remember { mutableStateOf("Tutti i settori") }
//    var settore by remember { mutableStateOf("") }
//    val settori: MutableList<String> = arrayListOf()
//
//    val vieNellaFalesia = viewModelVia.state
//
//    val vieDaVisualizzare = viewModelVia.state
//
////    var vieDaVisualizzare: MutableList<Via> = ArrayList()
////
////    for (i in vieNellaFalesia) {
////        vieDaVisualizzare.add(i)
////    }
//
//
//    if (settori.size <= 1) {
//        for (i in vieNellaFalesia.items) {
//            if (i.settore != settore) {
//                settore = i.settore
//                settori.add(i.settore)
//            }
//        }
//        settori.add("Tutti i settori")
//    }
//
//
//
//    if (settori.size > 1) {
//        val apriMenuSettori = remember { mutableStateOf(false) }
//        LazyColumn(
//            modifier = Modifier
//                .padding(top = paddingValues.calculateTopPadding())
//                .onGloballyPositioned { coordinates ->
//                    // Set column height using the LayoutCoordinates
//                    columnHeightDp = with(localDensity) { coordinates.size.height.toDp() }
//                }
//        ) {
//            item {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(start = 16.dp)
//                ) {
//                    TextButton(
//                        onClick = {
//                            apriMenuSettori.value = !apriMenuSettori.value
//                        }
//                    ) {
//                        Text(
//                            text = titoloSettore.value,
//                            fontSize = 20.sp
//                        )
//                    }
//                }
//            }
//
//            if (apriMenuSettori.value) {
//                settori.forEach() { settore ->
//                    item {
//                        TextButton(
//                            onClick = {
//                                titoloSettore.value = settore
//                                viewModelVia.getVieFalesiaSettore(idFalesia,titoloSettore.value)
//                                apriMenuSettori.value = !apriMenuSettori.value
//                            }
//                        ) {
//                            Text(text = settore)
//                        }
//                    }
//                }
//            }
//
//
//        }
//    }
//
//    //val vieDaVisualizzare: MutableList<Via> = viewModelVia.getVieFalesiaSettore(idFalesia,titoloSettore.value)
//
//    //viewModelVia.getVieFalesiaSettore(idFalesia,titoloSettore.value)
//
//
//    Log.d("VIE DA VISUALIZZARE", vieDaVisualizzare.items.size.toString())
//
////    vieDaVisualizzare = arrayListOf()
////    for (i in vieNellaFalesia) {
////        if (i.settore == titoloSettore.value) {
////            vieDaVisualizzare.add(i)
////        }
////        if (titoloSettore.value == "Tutti i settori") vieDaVisualizzare.add(i)
////    }
//
//
//    LazyColumn(
//        modifier = Modifier
//            .padding(top = secondPadding + columnHeightDp)
//    ) {
//
//
//        items(items = vieDaVisualizzare.items){via ->
//            ListItem(
//                via,
//                navController,
//
//                )
//
//        }
//
//
//
////        items(items = vieDaVisualizzare.sortedBy { it.numero }) { via ->
////
////            //var viaTemp: ViaScalata = ViaScalata()
////            var viaGiaScalata = false
////
////            for (i in arrayVieScalateUser) {
////                if (i.id == via.id) {
////                    //viaTemp = i
////                    viaGiaScalata = true
////                    break
////                }
////            }
////
//////            Log.d("VIATEMP", "id viatemp ${viaTemp.id}")
//////            Log.d("VIATEMP", "numero di scalate ${viaTemp.dataRipetizioni.size}")
////
////
////            //ListItem(via, navController, viaTemp)
////            ListItem(via, navController, viaGiaScalata)
////        }
//
//
//    }
//
//
//}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable

    fun ListItem(
    via: Via,
    navController: NavHostController,
    ) {
    //var pressioneIdVia by remember { mutableStateOf("") }
    //val arrayDateScalata = viaScalata.dataRipetizioni
    val expanded = remember { mutableStateOf(false) }
    val extraPadding by animateDpAsState(
        if (expanded.value) 24.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = ""
    )

//    var dataLetta by remember {
//        mutableStateOf("")
//    }
//    var dataLetta = ""

    val userHashMap = hashMapOf<String, Any>("vieScalate" to arrayVieScalateUser)
//    if (dataLetta != ""){
//        for (i in 0 until arrayVieScalateUser.size) {
//            if (arrayVieScalateUser[i].id == via.id) {
//                arrayVieScalateUser[i].dataRipetizioni.add(dataLetta)
//                //userHashMap["vieScalate"] = arrayVieScalateUser
//                dataLetta = ""
//                FirestoreClass().updateUserProfileData(userHashMap)
//            }
//        }
//    }

    //val arrayDate by calcolaArrayDate(arrayVieScalateUser, dataLetta, via)

    val viaPresente = remember { mutableStateOf(false) }

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
    //var caricaHashMap = remember { mutableStateOf(false) }


    var showDatePicker by remember {
        mutableStateOf(false)
    }
    //val userHashMap= ("vieScalate" arrayVieScalateUser)

    //val userHashMap = HashMap<String, Any>()

    //val userHashMap = remember {mutableStateMapOf<String, Any>()}

    //var userHashMap = remember { mutableStateOf(HashMap<String, Any>()) }


//    if (viaGiaScalata) {
//        viaPresente.value = !viaPresente.value
//        bordo = BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.primary)
//        stella = Icons.Default.Star
//    }


//    if (showDatePicker) {
//        MyDatePickerDialog(
//            onDateSelected = {
//                dataLetta = it
//                if (stella == stellaOFF) {
//                    stella = stellaON
//                    bordo = bordoON
//                }
//                //TODO aggiungere la via scalata alle vie scalate dall'utente
//
//            }
//        ) {
//            showDatePicker = false
//
//        }
//    }



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
                                if (via.protezioni == 0) {
                                    Text(text = "Protezioni ")
                                    Text(text = "---")
                                } else {
                                    Text(text = "Protezioni ")
                                    Text(
                                        text = "${via.protezioni}m",
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
                                        onClick = { Log.d("TEST", "CLICK PRESS") },
                                        onLongClick = {
//                                            showDatePicker = true
                                            //TODO controllo se la stella è piena
                                            if (stella != Icons.Default.Star) {
                                                Log.d("TEST", "LONG CLICK PRESS")
                                                arrayVieScalateUser.add(ViaScalata(via.id))
                                                FirestoreClass().updateUserProfileData(userHashMap)
                                                if (calcolaGrado(via.grado).toInt() > calcolaGrado(
                                                        userCorrente.gradoMax
                                                    ).toInt()
                                                ) {
                                                    val userHashMap2 =
                                                        hashMapOf<String, Any>("gradoMax" to via.grado)
                                                    Log.d(
                                                        "GRADO VIA",
                                                        "grado selezionato MAGGIORE del precedente"
                                                    )
                                                    Log.d(
                                                        "GRADO VIA",
                                                        "${calcolaGrado(via.grado).toString()} - ${
                                                            calcolaGrado(userCorrente.gradoMax).toString()
                                                        }  "
                                                    )
                                                    FirestoreClass().updateUserProfileData(
                                                        userHashMap2
                                                    )
                                                } else {
                                                    Log.d(
                                                        "GRADO VIA",
                                                        "grado selezionato minore del precedente"
                                                    )
                                                    Log.d(
                                                        "GRADO VIA",
                                                        "${calcolaGrado(via.grado).toString()} - ${
                                                            calcolaGrado(userCorrente.gradoMax).toString()
                                                        }  "
                                                    )
                                                }
                                            }

                                            //TODO LONG CLICK
                                            //Apri il pannello per scegliere la data

                                        }
                                    ),
                                imageVector = stella,
                                contentDescription = "stella"
                            )
                        }


                    }

                    var applicationContext = LocalContext.current


                    //TODO se l'utente ha già scalato la via aggiungi il divisore e le ripetizioni

//                    if (viaPresente.value) {
//                        Divider(
//                            thickness = 1.dp,
//                            color = MaterialTheme.colorScheme.inversePrimary
//                        )
//                        Row(
//                            modifier = Modifier.wrapContentHeight()
//                        ) {
//                            Text(text = "Ripetizioni: ")
//                        }
//                        if (!arrayDateScalata.isNullOrEmpty()) {
//                            for (i in arrayDateScalata) {
//                                Row() {
//                                    Text(
//                                        fontSize = 10.sp,
//                                        text = i
//                                    )
//                                }
//                            }
//                        }
//                        viaPresente.value = !viaPresente.value
//                    }


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

}


fun calcolaArrayDate(
    arrayVieScalateUser: MutableList<ViaScalata>,
    dataLetta: String,
    via: Via
): MutableList<ViaScalata> {
    val userHashMap = HashMap<String, Any>()

    for (i in 0..arrayVieScalateUser.size - 1) {
        arrayVieScalateUser.contains(ViaScalata(via.id))
        if (arrayVieScalateUser[i].id == via.id) {
            //Log.d("Array prima", arrayVieScalateUser[i].dataRipetizioni.size.toString())

            if (!arrayVieScalateUser[i].dataRipetizioni.contains(dataLetta)) {
                arrayVieScalateUser[i].dataRipetizioni.add(dataLetta)
                userHashMap.clear()
                userHashMap["vieScalate"] = arrayVieScalateUser
                FirestoreClass().updateUserProfileData(userHashMap)
                break
            }

            userHashMap.clear()
            //userHashMap["vieScalate"] = arrayVieScalateUser
            //userHashMap["vieScalate"] = arrayVieScalateUser

            //FirestoreClass().updateUserProfileData(userHashMap)
            //FirestoreClass().updateUserProfileData( hashMapOf("vieScalate",arrayVieScalateUser))
            //Log.d("Array dopo", arrayVieScalateUser[i].dataRipetizioni.size.toString())
            break

        }
        userHashMap.clear()
    }
    return arrayVieScalateUser

}


fun caclocaArrayVie(
    idVia: String,
    arrayVieScalateUser: ArrayList<ViaScalata>
): ArrayList<String> {
    var arrayDateScalata: ArrayList<String> = ArrayList()

    for (i in arrayVieScalateUser) {
        if (i.id == idVia) {
            for (x in i.dataRipetizioni) {
                arrayDateScalata.add(x)
            }
        }
    }
    return arrayDateScalata
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerView() {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        DatePicker(
            state = datePickerState
        )
        Spacer(
            modifier = Modifier.height(
                32.dp
            )
        )
        Button(
            onClick = {
                /*TODO*/
            }) {
            Text(
                text = selectedDate.toString(),
                color = Color.Red
            )
        }

    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(Date(millis))
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}


@Composable
fun MyDatePickerDialog(): String {
    var date by remember {
        mutableStateOf("Open date picker dialog")
    }

    var showDatePicker by remember {
        mutableStateOf(true)
    }

//    Box(contentAlignment = Alignment.Center) {
//        Button(onClick = { showDatePicker = true }) {
//            Text(text = date)
//        }
//    }

    if (showDatePicker) {
        MyDatePickerDialog(
            onDateSelected = { date = it },
            onDismiss = {
                showDatePicker = false
                date = ""
            }
        )
    }
    return date
}




