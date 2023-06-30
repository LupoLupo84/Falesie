package com.example.falesie.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FalesieScreen(navController: NavHostController) {
    val scrollBehaivor = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            modalDrawerSheetMenu(navController = navController)
        }
    ){
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
                CustomList(paddingValues = it)
                //LoginFrame(paddingValues = it, auth, navController)
            }
        )


    }



//    Box(
//        modifier = Modifier
//            .fillMaxSize(),
//    contentAlignment = Alignment.Center){
//        Text(text = "Falesie")
//    }
}



// TEMP PER LAZY LIST
@Composable
fun CustomList(paddingValues: PaddingValues) {
    val numbers = remember { mutableStateListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14) }

    LazyColumn(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        items(items = numbers, key = { it.hashCode() }) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 24.dp),
                text = "$it",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
// FINE