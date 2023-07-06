package com.example.falesie

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.falesie.firestore.FirestoreClass
import com.example.falesie.model.Falesia
import com.example.falesie.model.User
import com.example.falesie.model.Via
import com.example.falesie.model.ViaScalata
import com.example.falesie.room.ContactDatabase
import com.example.falesie.room.ContactViewModel
import com.example.falesie.room.Contactscreen
import com.example.falesie.screen.DbRoomScreen
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

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "contacts.db"
        ).build()
    }
    private val viewModel by viewModels<ContactViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ContactViewModel(db.dao) as T
                }
            }
        }
    )

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
                    //startDestination = startDestination                      //"LoginScreen"
                    //test applicazione
                    startDestination = "DbRoomScreen"
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
                    composable("DbRoomScreen") {
                        //DbRoomScreen(navController)

                        //test database room
                        val state by viewModel.state.collectAsState()
                        Contactscreen(state = state , onEvent = viewModel::onEvent)

                    }


                }


            }
        }

    }


}
