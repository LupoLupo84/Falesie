package com.example.falesie

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.falesie.firestore.FirestoreClass
import com.example.falesie.model.Falesia
import com.example.falesie.model.User
import com.example.falesie.model.Via
import com.example.falesie.model.ViaScalata
import com.example.falesie.screen.LoginScreen
import com.example.falesie.screen.FalesieScreen
import com.example.falesie.screen.GestioneFalesieScreen
import com.example.falesie.screen.ProfiloScreen
import com.example.falesie.screen.RegisterScreen
import com.example.falesie.screen.VieScreen
import com.example.falesie.ui.theme.FalesieTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {

    companion object {
        val auth by lazy { Firebase.auth }
        lateinit var userCorrente: User
        //var userCorrente: User = User()
        var listaVie: ArrayList<Via> = ArrayList()                          // Tutte le vie presenti nel db
        var listaFalesie: ArrayList<Falesia> = ArrayList()                  // Tutte le falesie presenti nel db
        var listaVieSelezionate: ArrayList<Via> = ArrayList()               // Vie presenti nella falesia corrente
        var falesiaSelezionata:Falesia = Falesia()                          // Falesia selezionata per la modifica
        var viaSelezionata : Via = Via()
        var arrayVieScalateUser: ArrayList<ViaScalata> = ArrayList()
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
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = startDestination                      //"LoginScreen"
                    //test applicazione
                    //startDestination = "RegisterScreen"
                ) {
                    composable("LoginScreen") {
                        LoginScreen(navController)
                    }
                    composable("FalesieScreen") {
                        FalesieScreen(navController)
                    }
                    composable("RegisterScreen") {
                        RegisterScreen(navController)
                    }
                    composable("ProfiloScreen") {
                        ProfiloScreen(navController)
                    }
                    composable("GestioneFalesieScreen") {
                        GestioneFalesieScreen(navController)
                    }
                    composable("VieScreen") {
                        VieScreen(navController)
                    }


                }


            }
        }

    }


}
