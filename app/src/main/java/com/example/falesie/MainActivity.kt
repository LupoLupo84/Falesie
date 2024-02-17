package com.example.falesie

//tutorial rooms db jetpack
//https://www.youtube.com/watch?v=voMTReNRvUA&list=PLUPcj46QWTDWlxtIwE3A6VEWUFEO8nh0Z&index=7

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.falesie.data.firestore.FirestoreClass
import com.example.falesie.data.firestore.model.Falesia
import com.example.falesie.data.firestore.model.User
import com.example.falesie.data.firestore.model.Via
import com.example.falesie.data.firestore.model.ViaScalata
import com.example.falesie.screen.LoginScreen
import com.example.falesie.screen.FalesieScreen
import com.example.falesie.screen.GestioneFalesieScreen
import com.example.falesie.screen.ModificaVieScreen
import com.example.falesie.screen.ProfiloScreen
import com.example.falesie.screen.RegisterScreen
import com.example.falesie.screen.VieScreen
//import com.example.falesie.screen.VieScreen
import com.example.falesie.ui.JetShopingNavigation
import com.example.falesie.ui.theme.FalesieTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var factory : FalesieViewModelFactory


    companion object {
        val auth by lazy { Firebase.auth }
        lateinit var userCorrente: User
        //var userCorrente = User()
        //var userCorrente: User = User()
        //var userCorrente = FirestoreClass().firstLoadUserData()
        var listaVie: ArrayList<Via> = ArrayList()                          // Firestore  Tutte le vie presenti nel db
        var listaFalesie: ArrayList<Falesia> = ArrayList()                  // Firestore  Tutte le falesie presenti nel db
//        var listaVieSelezionate: ArrayList<Via> = ArrayList()               // Vie presenti nella falesia corrente
//        var falesiaSelezionata:Falesia = Falesia()                          // Falesia selezionata per la modifica
//        var viaSelezionata : Via = Via()
        //var arrayVieScalateUser: ArrayList<ViaScalata> = ArrayList()
var arrayVieScalateUser: MutableList<ViaScalata> = arrayListOf()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val currentUserID = FirestoreClass().getCurrentUserID()
        Log.i("CURRENT USER ID", currentUserID)

        userCorrente = FirestoreClass().firstLoadUserData()
        Log.d("TEST", userCorrente.email)

        var startDestination = ""
        if (currentUserID.isNotEmpty()) {       // se ho caricato i dati dell'utente
            startDestination = "FalesieScreen"
        } else {                                // chiedo di effettuare il login
            startDestination = "LoginScreen"
        }


        setContent {
            FalesieTheme() {
                Log.i("START DESTINATION", startDestination)


                //MODIFICHE PER TEST
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background)
                {
                    //HomeScreen(onNavigate = {})      //navigationHost
                    //JetShoppingApp()                                                  //AVVIA LA APP JETSHOPPING
                }






                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = startDestination                      //"LoginScreen"
                    //test applicazione
                    //startDestination = "DbRoomScreen"
                ) {
                    composable("LoginScreen") {
                        LoginScreen(navController)
                    }
                    composable("FalesieScreen") {
                        Constants.SETTORECORRENTE = "Tutti i settori"
                        FalesieScreen(navController, factory)
                    }
                    composable("RegisterScreen") {
                        RegisterScreen(navController)
                    }
                    composable("ProfiloScreen") {
                        ProfiloScreen(navController)
                    }
                    composable("GestioneFalesieScreen") {
                        GestioneFalesieScreen(navController, factory)
                    }
                    // esempio navArgument https://www.reddit.com/r/JetpackCompose/comments/15gieu3/how_to_pass_arguments_to_a_composable_using/
                    composable(
                        route = "${"VieScreen"}/{nomeFalesia}",
                        arguments = listOf(
                            navArgument("nomeFalesia") { type = NavType.StringType }
                        )
                    ){backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val nomeFalesia =
                            arguments.getString("nomeFalesia") ?: error("")
                        val falesieViewModel : FalesieViewModel = viewModel(factory = factory)
                        val vieNelSettore = falesieViewModel.vieNellaFalesiaSettore.collectAsState(initial = emptyList())
                        val vieNellaFalesia = falesieViewModel.vieNellaFalesia.collectAsState(initial = emptyList())
                        //VieScreen(navController, nomeFalesia, factory, falesieViewModel, vieNelSettore)
                        VieScreen(navController, nomeFalesia, factory, falesieViewModel, vieNellaFalesia)
                        //VieScreen(navController, nomeFalesia, factory, falesieViewModel)
                    }

                    //MODIFICA VIE
                    composable(
                        route = "${"ModificaVieScreen"}/{nomeFalesia}",
                        arguments = listOf(
                            navArgument("nomeFalesia") { type = NavType.StringType }
                        )
                    ){backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val nomeFalesia =
                            arguments.getString("nomeFalesia") ?: error("")
                        val falesieViewModel : FalesieViewModel = viewModel(factory = factory)
                        //val vieNelSettore = falesieViewModel.vieNellaFalesiaSettore.collectAsState(initial = emptyList())
                        val vieNellaFalesia = falesieViewModel.vieNellaFalesia.collectAsState(initial = emptyList())
                        //VieScreen(navController, nomeFalesia, factory, falesieViewModel, vieNelSettore)
                        ModificaVieScreen(navController, nomeFalesia, factory, falesieViewModel, vieNellaFalesia)
                        //VieScreen(navController, nomeFalesia, factory, falesieViewModel)
                    }

                    }


                }


            }
        }

    }









    @Composable
    fun JetShoppingApp(){
        JetShopingNavigation()
    }


