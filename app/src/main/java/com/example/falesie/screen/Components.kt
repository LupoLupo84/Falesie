package com.example.falesie.screen

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BlurLinear
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.falesie.MainActivity
import com.example.falesie.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Contenuto del manu laterale
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun modalDrawerSheetMenu(navController: NavHostController) {
    val menuList = listOf(Icons.Filled.BlurLinear to "Falesie", Icons.Filled.Logout to "Logout")  //eventlist  //Wysiwyg
    var selectedItems by remember { mutableStateOf(-1) }
    var context = LocalContext.current

    ModalDrawerSheet {
        menuList.forEachIndexed { index, data ->
            NavigationDrawerItem(
                modifier = Modifier.padding(top = 16.dp),
                icon = {Icon(imageVector = data.first, contentDescription = data.second)},
                label = {Text(text = data.second) },
                selected = selectedItems == index,
                onClick = {
                    selectedItems = index
                    if (index == 0){        // Falesie
                        navController.popBackStack()
                        navController.navigate("FalesieScreen")
                    }
                    if (index == 1){        // Falesie
                        //TODO logout utente
                        MainActivity.auth.signOut()
                        Toast.makeText(
                            context,
                            context.resources.getString(R.string.disconnessione_eseguita),
                            Toast.LENGTH_LONG
                        ).show()
                        navController.popBackStack()
                        navController.navigate("LoginScreen")
                    }
                })
        }

    }
}

// Contenuto della top bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBarCustom(scrollBehaivor: TopAppBarScrollBehavior, scope: CoroutineScope, drawerState: DrawerState, titolo: String){
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
            Text( text = titolo)
        },
//                     colors = TopAppBarDefaults.smallTopAppBarColors(
//                         containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
//                     )
    )

}

