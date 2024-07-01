package com.example.falesie.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.falesie.Constants
import com.example.falesie.FalesieViewModel
import com.example.falesie.FalesieViewModelFactory
import com.example.falesie.data.room.models.Via
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun VieScreen(
    navController: NavHostController,
    nomeFalesia: String,
    factory: FalesieViewModelFactory,
    falesieViewModel: FalesieViewModel = viewModel(factory = factory),
//    vieNellaFalesia: State<List<Via>>,
    vieNellaFalesia: List<Via>,
    //onSelectVia: (via: Via) -> Unit,
) {
    val showPopup = remember { mutableStateOf(false) }
    //val vieNellaFalesia = falesieViewModel.vieNellaFalesia.collectAsState(initial = emptyList())
    Log.d("Vie nella falesia", vieNellaFalesia.size.toString())

    val settoriFalesia: MutableList<String> = mutableListOf("Tutti i settori")

    var tempSettoreCorrente = Constants.SETTORECORRENTE

    vieNellaFalesia.sortedBy { it.settore }

    for (i in vieNellaFalesia) {
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

                items(vieNellaFalesia.size) {
                    //visualizza solamente le vie della falesia corrispondenti al settore corrente oppure se è selezionato il settore tutti i settori
                    if (vieNellaFalesia[it].settore == Constants.SETTORECORRENTE || Constants.SETTORECORRENTE == "Tutti i settori") {
                        val context = LocalContext.current

                        val expanded = rememberSaveable { mutableStateOf(false) }
                        val extraPadding by animateDpAsState(
                            if (expanded.value) 24.dp else 0.dp,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ), label = ""
                        )


                        val bordoOFF =
                            BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.inverseSurface
                            )
                        val stellaOFF = Icons.Default.StarBorder
                        var bordo by remember { mutableStateOf(bordoOFF) }
                        var stella by remember { mutableStateOf(stellaOFF) }
                        var visualizza by remember { mutableStateOf(false) }
                        var showPopupYesNo by remember { mutableStateOf(false) }





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
                                        Row {
                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                            ) {
                                                Row {
                                                    Text(
                                                        text = vieNellaFalesia[it].settore,
                                                        fontSize = 12.sp,
                                                        color = Color.White,
                                                        style = TextStyle(
                                                            shadow = Shadow(
                                                                color = Color.Black,
                                                                offset = Offset(2f,2f),
                                                                blurRadius = 2f
                                                            )
                                                        )
                                                    )
                                                }
                                                Row {
                                                    Text(
                                                        modifier = Modifier.combinedClickable(
                                                            onClick = { /* Handle single click */ },
                                                            //onLongClick = { pressioneProlungataNome(vieNellaFalesia[it]) },
                                                            onLongClick = { /* Handle long click */  },
                                                            onDoubleClick = { showPopupYesNo = true  }
                                                        ),
                                                        text = "${vieNellaFalesia[it].numero} - ${vieNellaFalesia[it].viaName}",
                                                        style = MaterialTheme.typography.titleMedium.copy(
                                                            color = Color.White,
                                                            fontWeight = FontWeight.Bold,
                                                            shadow = Shadow(
                                                                color = Color.Black,
                                                                offset = Offset(2f,2f),
                                                                blurRadius = 2f
                                                            )
                                                        ),
                                                        fontSize = 18.sp
                                                    )

                                                }
                                            }
                                            Column(
                                            ) {

                                                IconButton(onClick = {
                                                    //visualizza = !visualizza
                                                    showPopup.value = !showPopup.value
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Filled.ImageSearch,
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
                                                Text(
                                                    text = "grado ${vieNellaFalesia[it].grado}",
                                                    fontSize = 14.sp
                                                )
                                            }

                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                            ) {
                                                Text(
                                                    text = "altezza ${vieNellaFalesia[it].altezza} m",
                                                    fontSize = 14.sp
                                                )
                                            }
                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                            ) {
                                                Text(
                                                    text = "protezioni ${vieNellaFalesia[it].protezioni}",
                                                    fontSize = 14.sp
                                                )
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
                                        text = "ID: ${vieNellaFalesia[it].id}"
                                    )
                                }

                                val storageDir: File = context.filesDir
                                //val new_file: File = File("$storageDir" + File.separator + "andraz1" +".webp")


                                val myDir: File = File(context.filesDir, "falesie")
                                val new_file: File = File("$myDir" + File.separator + vieNellaFalesia[it].immagine +".webp")

                                Log.d("FILES PATH", new_file.absolutePath)




                                    val immagine = new_file.absolutePath.toString()
                                    if (showPopup.value) {
                                        //ScalableImagePopup(imageUri = "your_image_url") { showPopup.value = false }
                                        ScalableImagePopup(imageUri = immagine) {
                                            showPopup.value = false
                                        }
                                    }


                                if (showPopupYesNo) {
                                    ConfirmationPopup(
                                        title = "Aggiunta via..",
                                        message = "Sei sicuro di aver scalato la via ",
                                        message2 = "${vieNellaFalesia[it].viaName}?",
                                        yesButtonLabel = "Si", // Custom label for Yes button
                                        noButtonLabel = "No",   // Custom label for No button
                                        onYesClicked = {
                                            // Handle "Yes" click
                                            showPopupYesNo = false
                                        },
                                        onNoClicked = {
                                            // Handle "No" click
                                            showPopupYesNo = false
                                        }
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


@Composable
fun ConfirmationPopup(
    title: String,
    message: String,
    message2: String,
    yesButtonLabel: String = "Yes", // Default label for Yes button
    noButtonLabel: String = "No",  // Default label for No button
    onYesClicked: () -> Unit,onNoClicked: () -> Unit
) {
    Dialog(onDismissRequest = { onNoClicked() }) { // Dismiss on background click or back press
        Card(
            elevation = CardDefaults.elevatedCardElevation(),
            shape = RoundedCornerShape(8.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = message)

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = message2,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onNoClicked) {
                        Text(noButtonLabel)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onYesClicked) {
                        Text(yesButtonLabel)
                    }
                }
            }
        }
    }
}





@Composable
fun ScalableImagePopup(
    imageUri: String, // Or use an ImageBitmap if you have the image loaded
    onDismiss: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember{ mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Log.d("nella funzione", "ScalableImagePopup")

    Dialog(onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false // Important: Disable platform default width
        )
        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                //.background(Color.Black.copy(alpha = 0.5f))
                .background(Color.Transparent)
                .clickable(onClick = onDismiss) // Close on background click
        ) {
            Column(
                modifier = Modifier
                    //.fillMaxWidth()
                    .fillMaxSize()
                    .padding(16.dp)
                    .align(Alignment.Center)
                    //.background(Color.White)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Scalable Image",
                    modifier = Modifier
                        //.fillMaxWidth()
                        .fillMaxSize()
                        .aspectRatio(1f) // Maintain aspect ratio
                        .scale(scale)
                        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                        .pointerInput(Unit) {
                            detectTransformGestures { centroid, pan, zoom, rotation ->
                                scale = (scale * zoom).coerceAtLeast(1f)
                                offsetX += pan.x
                                offsetY += pan.y
                            }
                        }
                )
            }
        }
    }
}




//fun scaricaImmaginiDaFirestore(context: Context) {
//    FirestoreClass().getImageFromStorage(context, "andraz1")
//    FirestoreClass().getImageFromStorage(context, "andraz2")
//    FirestoreClass().getImageFromStorage(context, "andraz3")
//    FirestoreClass().getImageFromStorage(context, "andraz4")
//    FirestoreClass().getImageFromStorage(context, "andraz5")
//    FirestoreClass().getImageFromStorage(context, "andraz6")
//    FirestoreClass().getImageFromStorage(context, "andraz7")
//    FirestoreClass().getImageFromStorage(context, "andraz8")
//    FirestoreClass().getImageFromStorage(context, "andraz9")
//    FirestoreClass().getImageFromStorage(context, "andraz10")
//    FirestoreClass().getImageFromStorage(context, "andraz11")
//    FirestoreClass().getImageFromStorage(context, "andraz12")
//}

//fun caricaImmaginiSuFirestore(context: Context) {
//    FirestoreClass().putImageInStorage(context, R.drawable.andraz1)
//    FirestoreClass().putImageInStorage(context, R.drawable.andraz2)
//    FirestoreClass().putImageInStorage(context, R.drawable.andraz3)
//    FirestoreClass().putImageInStorage(context, R.drawable.andraz4)
//    FirestoreClass().putImageInStorage(context, R.drawable.andraz5)
//    FirestoreClass().putImageInStorage(context, R.drawable.andraz6)
//    FirestoreClass().putImageInStorage(context, R.drawable.andraz7)
//    FirestoreClass().putImageInStorage(context, R.drawable.andraz8)
//    FirestoreClass().putImageInStorage(context, R.drawable.andraz9)
//    FirestoreClass().putImageInStorage(context, R.drawable.andraz10)
//    FirestoreClass().putImageInStorage(context, R.drawable.andraz11)
//    FirestoreClass().putImageInStorage(context, R.drawable.andraz12)
//}


//@Composable
//fun onSelectVia(via: Via) {
//    Log.d("Via selezionata ", via.viaName)
//    Image(
//        painter = painterResource(id = R.drawable.arrow_up_float),
//        contentDescription = "andraz")
//}


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







