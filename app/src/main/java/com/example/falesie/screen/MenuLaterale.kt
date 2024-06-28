package com.example.falesie.screen


import android.content.Context
import android.content.DialogInterface
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.BlurLinear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.falesie.MainActivity
import com.example.falesie.MainActivity.Companion.userCorrente
import com.example.falesie.MenuItem
import com.example.falesie.R
import com.example.falesie.data.firestore.FirestoreClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


// Contenuto del menu laterale
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDrawerSheetMenu(
    navController: NavHostController,
    //onEvent: ((ViarEvent) -> Unit)?
) {
    val myContext = LocalContext.current

    val listaDeiMenu = listOf(
        MenuItem(id = "Profilo", title = "Profilo", "Profilo", Icons.Filled.Person, false),
        MenuItem(id = "Falesie", title = "Falesie", "Falesie", Icons.Filled.BlurLinear, false),
        MenuItem(
            id = "GestioneFalesie",
            title = "GestioneFalesie",
            "GestioneFalesie",
            Icons.Filled.Settings,
            true
        ),
        MenuItem(id = "Logout", title = "Logout", "Logout", Icons.Filled.Logout, false),
        MenuItem(id = "DbRoom", title = "DbRoom", "DbRoom", Icons.Filled.SaveAlt, true),
        MenuItem(id = "DownloadImmagini", title = "DownloadImmagini", "DownloadImmagini", Icons.Filled.Download, false),
        MenuItem(id = "UploadImagini", title = "UploadImagini", "UploadImagini", Icons.Filled.Upload, false),
        MenuItem(id = "TEST", title = "Test", "Test", Icons.Filled.AcUnit, false),
    )
    var selectedItems by remember { mutableStateOf("") }



    ModalDrawerSheet {

        Column(
            modifier = Modifier.fillMaxSize(),
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (FirestoreClass().getCurrentUserID().isNotEmpty()) {
                DrawProfileUser()
            }


            listaDeiMenu.forEachIndexed { index, menuItem ->
                if (menuItem.admin && !userCorrente.admin) {
                    return@forEachIndexed
                }
                if (userCorrente.id.isEmpty()) {
                    return@forEachIndexed
                }
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = menuItem.icon,
                            contentDescription = menuItem.contentDescription
                        )
                    },
                    label = { Text(text = menuItem.title) },
                    selected = selectedItems == menuItem.contentDescription,
                    onClick = {
                        selectedItems = menuItem.contentDescription
                    })
            }

            if (selectedItems.isNotEmpty()) {
                when (selectedItems) {

                    "Profilo" -> {
                        Log.d("SELEZIONE", selectedItems)
                        selectedItems = ""
                        navController.popBackStack()
                        navController.navigate("ProfiloScreen")
                    }

                    "Falesie" -> {
                        Log.d("SELEZIONE", selectedItems)
                        selectedItems = ""
                        navController.popBackStack()
                        navController.navigate("FalesieScreen")
                    }

                    "GestioneFalesie" -> {
                        Log.d("SELEZIONE", selectedItems)
                        selectedItems = ""
                        navController.popBackStack()
                        navController.navigate("GestioneFalesieScreen")
                    }

                    "Logout" -> {
                        Log.d("SELEZIONE", selectedItems)
                        selectedItems = ""


                        android.app.AlertDialog.Builder(LocalContext.current)
                            .setTitle("Logout")
                            .setMessage("Sei sicuro di eseguire il logout?")
                            .setPositiveButton(
                                "Si",
                                DialogInterface.OnClickListener { dialog, which ->
                                    // logout
                                    //MainActivity.auth.signOut()
                                    MainActivity::signOut


                                    Log.d("userCorrente", userCorrente.toString())
                                    //TODO cancella tutto il database all'uscita dell'account
                                    //cancellaDatabase(myContext)
                                    userCorrente = userCorrente.copy(id = "")
                                    navController.popBackStack()
                                    navController.navigate("LoginScreen")

                                })
                            .setNegativeButton(
                                "No",
                                DialogInterface.OnClickListener { dialog, which ->
                                    // user doesn't want to logout
                                })
                            .show()
                    }

                    "DbRoom" -> {
                        Log.d("SELEZIONE", selectedItems)
                        selectedItems = ""
                        navController.popBackStack()
                        navController.navigate("DbRoomScreen")


                    }

                    "DownloadImmagini" -> {
                        Log.d("SELEZIONE", selectedItems)
                        selectedItems = ""
                        //navController.popBackStack()
                        //navController.navigate("DbRoomScreen")
                        var context = LocalContext.current
                        scaricaTutteLeImmagini(context)
                    }

                    "UploadImagini" -> {
                        Log.d("SELEZIONE", selectedItems)
                        selectedItems = ""
                        //navController.popBackStack()
                        //navController.navigate("DbRoomScreen")
                        var context = LocalContext.current
                        caricaTutteLeImmagini(context)
                    }

                    "Test" -> {
                        Log.d("SELEZIONE", selectedItems)
                        selectedItems = ""

                        val setAggiorna: HashMap<String, Any> = hashMapOf("aggiorna" to true)
                        FirestoreClass().updateUserProfileData(
                            setAggiorna
                        )

//                        android.app.AlertDialog.Builder(LocalContext.current)
//                            .setTitle("Logout")
//                            .setMessage("Sei sicuro di eseguire il logout? \nIl database verrà cancellato")
//                            .setPositiveButton(
//                                "Si",
//                                DialogInterface.OnClickListener { dialog, which ->
//                                    // logout
//                                    MainActivity.auth.signOut()
//                                    //userCorrente = User()
//                                    //MainActivity::signOut
//                                    Log.d("userCorrente", userCorrente.toString())
//                                    //TODO cancella tutto il database all'uscita dell'account
//                                    navController.popBackStack()
//                                    navController.navigate("LoginScreen")
//                                })
//                            .setNegativeButton(
//                                "No",
//                                DialogInterface.OnClickListener { dialog, which ->
//                                    // user doesn't want to logout
//                                })
//                            .show()
                    }
                }
            }
        }
    }
}

fun caricaTutteLeImmagini(context: Context) {
    FirestoreClass().putImageInStorage(context, R.drawable.andraz1)
    FirestoreClass().putImageInStorage(context, R.drawable.andraz2)
    FirestoreClass().putImageInStorage(context, R.drawable.andraz3)
    FirestoreClass().putImageInStorage(context, R.drawable.andraz4)
    FirestoreClass().putImageInStorage(context, R.drawable.andraz5)
    FirestoreClass().putImageInStorage(context, R.drawable.andraz6)
    FirestoreClass().putImageInStorage(context, R.drawable.andraz7)
    FirestoreClass().putImageInStorage(context, R.drawable.andraz8)
    FirestoreClass().putImageInStorage(context, R.drawable.andraz9)
    FirestoreClass().putImageInStorage(context, R.drawable.andraz10)
    FirestoreClass().putImageInStorage(context, R.drawable.andraz11)
    FirestoreClass().putImageInStorage(context, R.drawable.andraz12)
    FirestoreClass().putImageInStorage(context, R.drawable.boscoverde1)
    FirestoreClass().putImageInStorage(context, R.drawable.boscoverde2)
    FirestoreClass().putImageInStorage(context, R.drawable.brustolade1)
    FirestoreClass().putImageInStorage(context, R.drawable.caleda1)
    FirestoreClass().putImageInStorage(context, R.drawable.caleda2)
    FirestoreClass().putImageInStorage(context, R.drawable.caleda3)
    FirestoreClass().putImageInStorage(context, R.drawable.capannabill1)
    FirestoreClass().putImageInStorage(context, R.drawable.capannabill2)
    FirestoreClass().putImageInStorage(context, R.drawable.corpassa1)
    FirestoreClass().putImageInStorage(context, R.drawable.crepanegra1)
    FirestoreClass().putImageInStorage(context, R.drawable.farenzena1)
    FirestoreClass().putImageInStorage(context, R.drawable.farenzena2)
    FirestoreClass().putImageInStorage(context, R.drawable.farenzena3)
    FirestoreClass().putImageInStorage(context, R.drawable.farenzena4)
    FirestoreClass().putImageInStorage(context, R.drawable.farenzena5)
    FirestoreClass().putImageInStorage(context, R.drawable.farenzena6)
    FirestoreClass().putImageInStorage(context, R.drawable.farenzena7)
    FirestoreClass().putImageInStorage(context, R.drawable.farenzena8)
    FirestoreClass().putImageInStorage(context, R.drawable.farenzena9)
    FirestoreClass().putImageInStorage(context, R.drawable.farenzena10)
    FirestoreClass().putImageInStorage(context, R.drawable.farenzena11)
    FirestoreClass().putImageInStorage(context, R.drawable.forte1)
    FirestoreClass().putImageInStorage(context, R.drawable.forte2)
    FirestoreClass().putImageInStorage(context, R.drawable.gares1)
    FirestoreClass().putImageInStorage(context, R.drawable.gares2)
    FirestoreClass().putImageInStorage(context, R.drawable.gares3)
    FirestoreClass().putImageInStorage(context, R.drawable.gares4)
    FirestoreClass().putImageInStorage(context, R.drawable.gares5)
    FirestoreClass().putImageInStorage(context, R.drawable.gares6)
    FirestoreClass().putImageInStorage(context, R.drawable.gares7)
    FirestoreClass().putImageInStorage(context, R.drawable.gares8)
    FirestoreClass().putImageInStorage(context, R.drawable.gares9)
    FirestoreClass().putImageInStorage(context, R.drawable.gares10)
    FirestoreClass().putImageInStorage(context, R.drawable.gares11)
    FirestoreClass().putImageInStorage(context, R.drawable.gares12)
    FirestoreClass().putImageInStorage(context, R.drawable.laghetti1)
    FirestoreClass().putImageInStorage(context, R.drawable.laghetti2)
    FirestoreClass().putImageInStorage(context, R.drawable.laghetti3)
    FirestoreClass().putImageInStorage(context, R.drawable.laghetti4)
    FirestoreClass().putImageInStorage(context, R.drawable.laghetti5)
    FirestoreClass().putImageInStorage(context, R.drawable.laghetti6)
    FirestoreClass().putImageInStorage(context, R.drawable.laste1)
    FirestoreClass().putImageInStorage(context, R.drawable.laste2)
    FirestoreClass().putImageInStorage(context, R.drawable.laste3)
    FirestoreClass().putImageInStorage(context, R.drawable.laste4)
    FirestoreClass().putImageInStorage(context, R.drawable.laste5)
    FirestoreClass().putImageInStorage(context, R.drawable.laste6)
    FirestoreClass().putImageInStorage(context, R.drawable.laste7)
    FirestoreClass().putImageInStorage(context, R.drawable.laste8)
    FirestoreClass().putImageInStorage(context, R.drawable.laste9)
    FirestoreClass().putImageInStorage(context, R.drawable.laste10)
    FirestoreClass().putImageInStorage(context, R.drawable.laste11)
    FirestoreClass().putImageInStorage(context, R.drawable.laste12)
    FirestoreClass().putImageInStorage(context, R.drawable.laste13)
    FirestoreClass().putImageInStorage(context, R.drawable.laste14)
    FirestoreClass().putImageInStorage(context, R.drawable.laste15)
    FirestoreClass().putImageInStorage(context, R.drawable.laste16)
    FirestoreClass().putImageInStorage(context, R.drawable.laste17)
    FirestoreClass().putImageInStorage(context, R.drawable.laste18)
    FirestoreClass().putImageInStorage(context, R.drawable.laste19)
    FirestoreClass().putImageInStorage(context, R.drawable.laste20)
    FirestoreClass().putImageInStorage(context, R.drawable.laste21)
    FirestoreClass().putImageInStorage(context, R.drawable.laste22)
    FirestoreClass().putImageInStorage(context, R.drawable.laste23)
    FirestoreClass().putImageInStorage(context, R.drawable.laste24)
    FirestoreClass().putImageInStorage(context, R.drawable.laste25)
    FirestoreClass().putImageInStorage(context, R.drawable.laste26)
    FirestoreClass().putImageInStorage(context, R.drawable.laste27)
    FirestoreClass().putImageInStorage(context, R.drawable.laste28)
    FirestoreClass().putImageInStorage(context, R.drawable.laste29)
    FirestoreClass().putImageInStorage(context, R.drawable.laste30)
    FirestoreClass().putImageInStorage(context, R.drawable.laste31)
    FirestoreClass().putImageInStorage(context, R.drawable.malgaciapela1)
    FirestoreClass().putImageInStorage(context, R.drawable.malgaciapela2)
    FirestoreClass().putImageInStorage(context, R.drawable.malgaciapela3)
    FirestoreClass().putImageInStorage(context, R.drawable.masare1)
    FirestoreClass().putImageInStorage(context, R.drawable.mesaroz1)
    FirestoreClass().putImageInStorage(context, R.drawable.mesaroz2)
    FirestoreClass().putImageInStorage(context, R.drawable.mesaroz3)
    FirestoreClass().putImageInStorage(context, R.drawable.mesaroz4)
    FirestoreClass().putImageInStorage(context, R.drawable.mesaroz5)
    FirestoreClass().putImageInStorage(context, R.drawable.mesaroz6)
    FirestoreClass().putImageInStorage(context, R.drawable.mesaroz7)
    FirestoreClass().putImageInStorage(context, R.drawable.mesaroz8)
    FirestoreClass().putImageInStorage(context, R.drawable.mesaroz9)
    FirestoreClass().putImageInStorage(context, R.drawable.mesaroz10)
    FirestoreClass().putImageInStorage(context, R.drawable.mesaroz11)
    FirestoreClass().putImageInStorage(context, R.drawable.mesaroz12)
    FirestoreClass().putImageInStorage(context, R.drawable.mesaroz13)
    FirestoreClass().putImageInStorage(context, R.drawable.mesaroz14)
    FirestoreClass().putImageInStorage(context, R.drawable.mezzocanale1)
    FirestoreClass().putImageInStorage(context, R.drawable.mezzocanale2)
    FirestoreClass().putImageInStorage(context, R.drawable.mezzocanale3)
    FirestoreClass().putImageInStorage(context, R.drawable.mezzocanale4)
    FirestoreClass().putImageInStorage(context, R.drawable.mezzocanale5)
    FirestoreClass().putImageInStorage(context, R.drawable.mezzocanale6)
    FirestoreClass().putImageInStorage(context, R.drawable.mezzocanale7)
    FirestoreClass().putImageInStorage(context, R.drawable.mezzocanale8)
    FirestoreClass().putImageInStorage(context, R.drawable.mezzocanale9)
    FirestoreClass().putImageInStorage(context, R.drawable.moiazzacarestiato1)
    FirestoreClass().putImageInStorage(context, R.drawable.moiazzacarestiato2)
    FirestoreClass().putImageInStorage(context, R.drawable.rifugioscarpa1)
    FirestoreClass().putImageInStorage(context, R.drawable.stresole1)
    FirestoreClass().putImageInStorage(context, R.drawable.stresole2)
}

fun scaricaTutteLeImmagini(context: Context) {
    FirestoreClass().getAllImageFromStorage(context)
}






// Contenuto della top bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBarCustom(
    scrollBehaivor: TopAppBarScrollBehavior,
    scope: CoroutineScope,
    drawerState: DrawerState,
    titolo: String
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
//                     colors = TopAppBarDefaults.smallTopAppBarColors(
//                         containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
//                     )
    )

}

@Composable
fun DrawProfileUser() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
    ) {

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
        ) {
            var immagine = ""
            if (userCorrente.immagine.isEmpty()) {
                immagine =
                    "https://www.gyfted.me/_next/image?url=%2Fimg%2Fcharacters%2Fmilhouse-van-houten.png&w=640&q=75"        //variabile usata per test immagine

            } else {
                immagine = userCorrente.immagine
            }

            Image(
                modifier = Modifier
                    .size(120.dp),
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(immagine)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(false)
                        }).build()
                ),
                contentDescription = "description",
                contentScale = ContentScale.Fit
            )

        }

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = "ID: " + userCorrente.id,
                fontSize = 6.sp
            )
            Text(
                text = userCorrente.nome,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Grado massimo",
                fontSize = 12.sp,
            )
            Text(
                text = userCorrente.gradoMax,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = "Vie scalate",
                fontSize = 12.sp,
            )
            Text(
                text = userCorrente.vieScalate.size.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

        }

    }
    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primary)
}

