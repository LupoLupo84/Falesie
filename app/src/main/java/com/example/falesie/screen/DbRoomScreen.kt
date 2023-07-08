package com.example.falesie.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.falesie.Aggiorna
import com.example.falesie.MainActivity.Companion.listaVie
import com.example.falesie.firestore.FirestoreClass
import com.example.falesie.room.ViarEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DbRoomScreen(
    navController: NavHostController,
    onEvent: (ViarEvent) -> Unit
){

    val scrollBehaivor = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheetMenu(navController = navController, onEvent)
        }
    ) {

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehaivor.nestedScrollConnection),
            topBar = {
                topAppBarCustom(
                    scrollBehaivor = scrollBehaivor,
                    scope = scope,
                    drawerState = drawerState,
                    titolo = "DbRoomScreen"
                )
            },
            content = {
                //CustomList(paddingValues = it)
                DbRoomFrame(paddingValues = it, navController, onEvent)

            }
        )


    }

}


@Composable
fun DbRoomFrame(
    paddingValues: PaddingValues,
    navController: NavHostController,
    onEvent: (ViarEvent) -> Unit
){

    var lista = remember { mutableStateListOf(listaVie) }

    //SalvaNelDbRoom(onEvent)

    Column(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding()),
    ) {
        Row() {
            if (lista.isEmpty()){
                Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        FirestoreClass().leggiTutteLeVie(object : Aggiorna {
                            override fun aggiorna() {
                                lista = mutableStateListOf(listaVie)
                                Log.i("VIE LETTE IF", lista.size.toString())

                            }
                        })
                    }) {
                    Icon(
                        modifier = Modifier.padding(end = 16.dp),
                        imageVector = Icons.Default.Save,
                        contentDescription = "button carica"
                    )
                    Text(text = "Leggi le vie sul database OnLine")
                }
            }
        }



        Row() {
            if (!lista.isEmpty()) {
                Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        onEvent(ViarEvent.SaveViar)
                    }) {
                    Icon(
                        modifier = Modifier.padding(end = 16.dp),
                        imageVector = Icons.Default.Save,
                        contentDescription = "button save"
                    )
                    Text(text = "Salva le vie lette nel database locale")
                }
            }
        }





    }
}


