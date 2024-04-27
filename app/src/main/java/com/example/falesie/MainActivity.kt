package com.example.falesie

//tutorial rooms db jetpack
//https://www.youtube.com/watch?v=voMTReNRvUA&list=PLUPcj46QWTDWlxtIwE3A6VEWUFEO8nh0Z&index=7


import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.falesie.screen.AggiornaViaScreen
import com.example.falesie.screen.FalesieScreen
import com.example.falesie.screen.GestioneFalesieScreen
import com.example.falesie.screen.LoginScreen
import com.example.falesie.screen.ModificaVieScreen
import com.example.falesie.screen.ProfiloScreen
import com.example.falesie.screen.RegisterScreen
import com.example.falesie.screen.VieScreen
import com.example.falesie.ui.JetShopingNavigation
import com.example.falesie.ui.theme.FalesieTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity() : ComponentActivity() {
    @Inject
    lateinit var factory: FalesieViewModelFactory

    companion object {
        val auth by lazy { Firebase.auth }
        lateinit var userCorrente: User


        var listaVie: ArrayList<Via> = ArrayList()                          // Firestore  Tutte le vie presenti nel db
        var listaFalesie: ArrayList<Falesia> = ArrayList()                  // Firestore  Tutte le falesie presenti nel db
        var arrayVieScalateUser: MutableList<ViaScalata> = arrayListOf()
    }


    fun signOut(){
        auth.signOut()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val currentUserID = FirestoreClass().getCurrentUserID()
        Log.i("CURRENT USER ID", currentUserID)

        userCorrente = FirestoreClass().firstLoadUserData()
        Log.d("TEST", userCorrente.email)

        //var startDestination = ""
        val startDestination = if (currentUserID.isNotEmpty()) {       // se ho caricato i dati dell'utente
            "FalesieScreen"
        } else {                                // chiedo di effettuare il login
            "LoginScreen"
        }


        setContent {
            FalesieTheme() {
                Log.i("START DESTINATION", startDestination)


                //MODIFICHE PER TEST
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    //HomeScreen(onNavigate = {})      //navigationHost
                    //JetShoppingApp()                                                  //AVVIA LA APP JETSHOPPING
                }



                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = startDestination                      //"LoginScreen"
                ) {
                    composable("LoginScreen") {
                        LoginScreen(navController, factory)
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
                    ) { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val nomeFalesia =
                            arguments.getString("nomeFalesia") ?: error("")
                        val falesieViewModel: FalesieViewModel = viewModel(factory = factory)
                        val vieNellaFalesia by falesieViewModel.vieNellaFalesia.observeAsState()
                        vieNellaFalesia?.let {it ->
                            VieScreen(
                                navController,
                                nomeFalesia,
                                factory,
                                falesieViewModel,
                                it
                            )
                        }
                    }

                    //MODIFICA VIE
                    composable(
                        route = "${"ModificaVieScreen"}/{nomeFalesia}",
                        arguments = listOf(
                            navArgument("nomeFalesia") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val nomeFalesia =
                            arguments.getString("nomeFalesia") ?: error("")
                        val falesieViewModel: FalesieViewModel = viewModel(factory = factory)
                        val vieNellaFalesia by falesieViewModel.vieNellaFalesia.observeAsState()
                        vieNellaFalesia?.let { it->
                            ModificaVieScreen(
                                navController,
                                nomeFalesia,
                                factory,
                                falesieViewModel,
                                it
                            )
                        }
                    }



                    //AGGIORNA VIA
                    composable(
                        route = "${"AggiornaViaScreen"}/{idVia}",
                        arguments = listOf(
                            navArgument("idVia") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val idVia =
                            arguments.getString("idVia") ?: error("")
                        val falesieViewModel: FalesieViewModel = viewModel(factory = factory)
                        val vieNellaFalesia by falesieViewModel.vieNellaFalesia.observeAsState()
                        falesieViewModel.getViaIdMod(idVia)
                        val viaDaMod by falesieViewModel.viaDaMod.observeAsState()
                        viaDaMod?.let {it ->
                            vieNellaFalesia?.let { it2 ->
                                AggiornaViaScreen(
                                    navController,
                                    idVia,
                                    factory,
                                    falesieViewModel,
                                    it2,    //passa vieNellaFalesia
                                    it      //passa viaDaMod
                                )
                            }
                        }
                    }



                    // se premo back e il mio navcontroller si trova nella pagina "FalesieScreen" termino la app e la rimuovo dallo stack
                    onBackPressedDispatcher.addCallback( /* lifecycle owner */) {
                        // Back is pressed... Finishing the activity
                        if (navController.currentDestination?.route.toString() == "FalesieScreen" ||
                            navController.currentDestination?.route.toString() == "LoginScreen"
                            ) {
                            Log.d(
                                "TERMINA APPLICAZIONE",
                                navController.currentDestination?.route.toString()
                            )
                            finishAndRemoveTask()
                        }
                    }






                }


            }


        }
    }












}







//finishAndRemoveTask()


@Composable
fun JetShoppingApp() {
    JetShopingNavigation()
}


