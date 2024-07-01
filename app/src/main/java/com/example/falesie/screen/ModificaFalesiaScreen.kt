package com.example.falesie.screen

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.falesie.Constants
import com.example.falesie.FalesieViewModel
import com.example.falesie.FalesieViewModelFactory
import com.example.falesie.data.firestore.FirestoreClass
import com.example.falesie.data.room.models.Via
import com.example.falesie.tool.EditaTesto
import com.example.falesie.tool.myCheckbox
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ModificaFalesiaScreen(
    navController: NavHostController,
    nomeFalesia: String,
    factory: FalesieViewModelFactory,
    falesieViewModel: FalesieViewModel = viewModel(factory = factory),
    //vieNellaFalesia: State<List<Via>>,
    vieNellaFalesia: List<Via>,
) {
    falesieViewModel.getFalesiaFromName(nomeFalesia)
    val falesia by falesieViewModel.falesia.observeAsState()
    var locazioneCambiata = remember { mutableStateOf(false) }
    //var falesia:Falesia = falesieViewModel.getFalesiaFromName(nomeFalesia)


    //val wInfo = LocalContext.current.applicationContext.getSystemService(Context.)
    //val macAddress = wInfo.
//    val android_device_id =
//        Settings.Secure.getString(Application().contentResolver, Settings.Secure.ANDROID_ID)


//    Log.d("MAC ADDRESS", android_device_id)

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
        falesia?.let {
            Log.d("FALESIA.LET", "LETTA OK")
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehaivor.nestedScrollConnection),
                topBar = {

                    TopAppBarFalesia(
                        scrollBehaivor = scrollBehaivor,
                        scope = scope,
                        drawerState = drawerState,
                        //titolo = nomeFalesia,
                        titolo = it.nome,
                    )

                }
            ) {
                val secondPadding = it.calculateTopPadding()
                val columnHeightDp by remember { mutableStateOf(0.dp) }
                val localDensity = LocalDensity.current
                val listSorted = vieNellaFalesia.toList()
                val context = LocalContext.current
                var latitudineRilevata = ""
                var longitudineRilevata = ""





                Surface(
                    //color = MaterialTheme.colorScheme.primary,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(
                            top = secondPadding, // + columnHeightDp,
                            start = 4.dp,
                            end = 4.dp
                        ),
                    shape = RoundedCornerShape(10.dp),
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp)
                    ) {


                        falesia!!.nome =
                            EditaTesto(
                                testo = "nome",
                                selezioneAttuale = falesia!!.nome,
                                fontSizeDescrizione = 12.sp,
                                startDescrizione = 5.dp,
                                fontSizeTesto = 20.sp,
                                startTesto = 5.dp
                            )

                        Row {
                            TextButton(onClick = {
                                // AGGIORNA LA LATITUDINE E LA LONGITUDINE CON LA POSIZIONE ATTUALE
                                val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

                                if (ActivityCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return@TextButton
                                }
                                fusedLocationClient.lastLocation
                                    .addOnSuccessListener { location: Location? ->
                                        if (location != null) {
                                            latitudineRilevata = location.latitude.toString()
                                            longitudineRilevata = location.longitude.toString()
                                            falesia!!.latitudine = location.latitude.toString()
                                            falesia!!.longitudine = location.longitude.toString()
                                            Log.d("Latitudine", location.latitude.toString())
                                            Log.d("Longitudine", location.longitude.toString())
                                            locazioneCambiata.value = true
                                            // Salva latitudine e longitudine, ad esempio in SharedPreferences o in un database
                                        } else {
                                            // Gestisci il caso in cui la posizione non è disponibile
                                        }
                                    }
                                    .addOnFailureListener { exception: Exception ->
                                        // Gestisci l'errore
                                    }


                            }) {
                                Icon(
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = "Localizzazione",
                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                )
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Localizzami")
                            }
                        }



                            falesia!!.latitudine =
                                EditaTesto(
                                    testo = "latitudine",
                                    selezioneAttuale = falesia!!.latitudine,
                                    fontSizeDescrizione = 12.sp,
                                    startDescrizione = 5.dp,
                                    fontSizeTesto = 20.sp,
                                    startTesto = 5.dp
                                ).replace(",", ".")

                            falesia!!.longitudine =
                                EditaTesto(
                                    testo = "longitudine",
                                    selezioneAttuale = falesia!!.longitudine,
                                    fontSizeDescrizione = 12.sp,
                                    startDescrizione = 5.dp,
                                    fontSizeTesto = 20.sp,
                                    startTesto = 5.dp
                                ).replace(",", ".")




                        if(locazioneCambiata.value){
                            falesia!!.latitudine =
                                EditaTesto(
                                    testo = "latitudine",
                                    selezioneAttuale = latitudineRilevata,
                                    fontSizeDescrizione = 12.sp,
                                    startDescrizione = 5.dp,
                                    fontSizeTesto = 20.sp,
                                    startTesto = 5.dp
                                )

                            falesia!!.longitudine =
                                EditaTesto(
                                    testo = "longitudine",
                                    selezioneAttuale = longitudineRilevata,
                                    fontSizeDescrizione = 12.sp,
                                    startDescrizione = 5.dp,
                                    fontSizeTesto = 20.sp,
                                    startTesto = 5.dp
                                )
                            locazioneCambiata.value = false
                        }





                        falesia!!.altitudine =
                            EditaTesto(
                                testo = "altitudine",
                                selezioneAttuale = falesia!!.altitudine.toString(),
                                fontSizeDescrizione = 12.sp,
                                startDescrizione = 5.dp,
                                fontSizeTesto = 20.sp,
                                startTesto = 5.dp
                            ).toInt()

                        falesia!!.descrizione =
                            EditaTesto(
                                testo = "descrizione",
                                selezioneAttuale = falesia!!.descrizione,
                                fontSizeDescrizione = 12.sp,
                                startDescrizione = 5.dp,
                                fontSizeTesto = 20.sp,
                                startTesto = 5.dp,
                                lineaSingola = false,
                            )

                        falesia!!.primavera =
                            myCheckbox(
                                testo = "Primavera",
                                selezioneAttuale = falesia!!.primavera,
                            )

                        falesia!!.estate =
                            myCheckbox(
                                testo = "Estate",
                                selezioneAttuale = falesia!!.estate,
                            )

                        falesia!!.autunno =
                            myCheckbox(
                                testo = "Autunno",
                                selezioneAttuale = falesia!!.autunno,
                            )

                        falesia!!.inverno =
                            myCheckbox(
                                testo = "Inverno",
                                selezioneAttuale = falesia!!.inverno
                            )



                        Row {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TextButton(
                                    onClick = {
                                        Log.d("Nome", falesia!!.nome)
                                        Log.d("Latitudine", falesia!!.latitudine)
                                        Log.d("Longitudine", falesia!!.longitudine)
                                        Log.d("Altitudine", falesia!!.altitudine.toString())
                                        Log.d("Descrizione", falesia!!.descrizione)
                                        Log.d("Primavera", falesia!!.primavera.toString())
                                        Log.d("Estate", falesia!!.estate.toString())
                                        Log.d("Autunno", falesia!!.autunno.toString())
                                        Log.d("Inverno", falesia!!.inverno.toString())
                                        //TODO AGGIORNARE FALESIA SU FIRESTORE


                                        val falesiaFirestore: com.example.falesie.data.firestore.model.Falesia =
                                            com.example.falesie.data.firestore.model.Falesia(
                                                id = falesia!!.id,
                                                nome = falesia!!.nome,
                                                latitudine = falesia!!.latitudine,
                                                longitudine = falesia!!.longitudine,
                                                altitudine = falesia!!.altitudine,
                                                descrizione = falesia!!.descrizione,
                                                primavera = falesia!!.primavera,
                                                estate = falesia!!.estate,
                                                autunno = falesia!!.autunno,
                                                inverno = falesia!!.inverno,

                                            )
                                        FirestoreClass().updateFalesia(falesiaFirestore,context)

                                        val setAggiorna: HashMap<String, Any> = hashMapOf("aggiorna" to true)
                                        FirestoreClass().updateUserProfileData(
                                            setAggiorna
                                        )

                                        navController.popBackStack()

                                    }

                                ) {
                                    Text(text = "Aggiorna")
                                }

                            }

                        }




                    }


                }
            }


        }
    }
}





