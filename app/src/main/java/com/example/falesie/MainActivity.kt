package com.example.falesie

//tutorial rooms db jetpack
//https://www.youtube.com/watch?v=voMTReNRvUA&list=PLUPcj46QWTDWlxtIwE3A6VEWUFEO8nh0Z&index=7

import android.os.Bundle
import android.util.Log
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import com.example.falesie.room.ViarDatabase
import com.example.falesie.room.ViarViewModel
import com.example.falesie.screen.DbRoomScreen
import com.example.falesie.screen.LoginScreen
import com.example.falesie.screen.FalesieScreen
import com.example.falesie.screen.GestioneFalesieScreen
import com.example.falesie.screen.ProfiloScreen
import com.example.falesie.screen.RegisterScreen
import com.example.falesie.screen.VieScreen
import com.example.falesie.screen.falesiaSelected
import com.example.falesie.ui.JetShopingNavigation
import com.example.falesie.ui.home.HomeScreen
import com.example.falesie.ui.theme.FalesieTheme
import com.google.android.gms.tasks.Tasks
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

    private val dbViar by lazy {
        Room.databaseBuilder(
            applicationContext,
            ViarDatabase::class.java,
            "via.db"
        ).build()
    }
    private val viewModelViar by viewModels<ViarViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ViarViewModel(dbViar.dao) as T
                }
            }
        }
    )

    companion object {
        val auth by lazy { Firebase.auth }
        lateinit var userCorrente: User
        //var userCorrente = User()
        //var userCorrente: User = User()
        //var userCorrente = FirestoreClass().firstLoadUserData()
        var listaVie: ArrayList<Via> = ArrayList()                          // Tutte le vie presenti nel db
        var listaFalesie: ArrayList<Falesia> = ArrayList()                  // Tutte le falesie presenti nel db
//        var listaVieSelezionate: ArrayList<Via> = ArrayList()               // Vie presenti nella falesia corrente
//        var falesiaSelezionata:Falesia = Falesia()                          // Falesia selezionata per la modifica
//        var viaSelezionata : Via = Via()
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
                        FalesieScreen(navController, onEvent = viewModelViar::onEvent)
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
                        VieScreen(navController, falesiaSelected)
                    }
                    composable("DbRoomScreen") {
                        DbRoomScreen(navController, onEvent = viewModelViar::onEvent)

                        //test database room
//                        val state by viewModel.state.collectAsState()
//                        Contactscreen(state = state , onEvent = viewModel::onEvent)

                    }


                }


            }
        }

    }








    @Composable
    fun JetShoppingApp(){
        JetShopingNavigation()
    }

}
