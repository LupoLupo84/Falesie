package com.example.falesie.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BlurLinear
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.falesie.MainActivity
import com.example.falesie.MainActivity.Companion.userCorrente
import com.example.falesie.MenuItem
import com.example.falesie.R
import com.example.falesie.firestore.FirestoreClass
import com.example.falesie.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


// Contenuto del manu laterale
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun modalDrawerSheetMenu(navController: NavHostController) {

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
    )
    var selectedItems by remember { mutableStateOf("") }
    var context = LocalContext.current



    ModalDrawerSheet {

        Column(
            modifier = Modifier.fillMaxSize(),
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (FirestoreClass().getCurrentUserID().isNotEmpty()) {
                drawProfileUser()
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
                    //selected = selectedItems == index,
                    selected = selectedItems == menuItem.contentDescription,
                    onClick = {
                        //selectedItems = index
                        selectedItems = menuItem.contentDescription
                    })
            }

            if (selectedItems.isNotEmpty()) {
                when (selectedItems) {

                    "Profilo" -> Log.d("SELEZIONE", selectedItems)
                    "Falesie" -> {
                        Log.d("SELEZIONE", selectedItems)
                        selectedItems = ""
                        navController.popBackStack()
                        navController.navigate("FalesieScreen")
                    }
                    "GestioneFalesie" -> Log.d("SELEZIONE", selectedItems)
                    "Logout" -> {
                        Log.d("SELEZIONE", selectedItems)
                        selectedItems = ""
                        MainActivity.auth.signOut()
                        userCorrente = User()
                        Toast.makeText(
                            context,
                            context.resources.getString(R.string.disconnessione_eseguita),
                            Toast.LENGTH_LONG
                        ).show()
                        navController.popBackStack()
                        navController.navigate("LoginScreen")
                    }


                }
            }


//            menuList.forEachIndexed { index, data ->
//                NavigationDrawerItem(
//                    modifier = Modifier.padding(top = 16.dp),
//                    icon = { Icon(imageVector = data.first, contentDescription = data.second) },
//                    label = { Text(text = data.second) },
//                    selected = selectedItems == index,
//                    onClick = {
//                        selectedItems = index
//                        if (index == 0) {        // Falesie
//                            navController.popBackStack()
//                            navController.navigate("FalesieScreen")
//                        }
//                        if (index == 1) {        // Logout
//                            //TODO logout utente
//                            MainActivity.auth.signOut()
//                            userCorrente = User()
//                            Toast.makeText(
//                                context,
//                                context.resources.getString(R.string.disconnessione_eseguita),
//                                Toast.LENGTH_LONG
//                            ).show()
//                            navController.popBackStack()
//                            navController.navigate("LoginScreen")
//                        }
//                    }
//                )
//            }


        }
    }
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
fun drawProfileUser() {
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
                            //placeholder(R.drawable.placeholderfoto_face_24)
                        }).build()
                ),
                contentDescription = "description",
                //contentScale = ContentScale.FillHeight
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

        //Spacer(modifier = Modifier.size(5.dp))

        //Spacer(modifier = Modifier.size(5.dp))

    }
    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primary)
}

