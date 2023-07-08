package com.example.falesie.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestioneFalesieScreen(navController: NavHostController){

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
                topAppBarCustom(
                    scrollBehaivor = scrollBehaivor,
                    scope = scope,
                    drawerState = drawerState,
                    titolo = "Gestione Falesie"
                )
            },
            content = {
                //CustomList(paddingValues = it)
                GestioneFalesieFrame(paddingValues = it, navController)

            }
        )


    }

}


@Composable
fun GestioneFalesieFrame(paddingValues: PaddingValues, navController: NavHostController){

}