package com.example.falesie

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.falesie.firestore.FirestoreClass
import com.example.falesie.model.User
import com.example.falesie.screen.LoginScreen
import com.example.falesie.screen.FalesieScreen
import com.example.falesie.screen.RegisterScreen
import com.example.falesie.ui.theme.FalesieTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {

    companion object{
        val auth by lazy { Firebase.auth }
        lateinit var userCorrente: User
        //var userCorrente: User = User()
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val currentUserID = FirestoreClass().getCurrentUserID()
        Log.i("CURRENT USER ID", currentUserID)

        var startDestination = ""
        if (currentUserID.isNotEmpty()) {
            FirestoreClass().firstLoadUserData()
            startDestination = "FalesieScreen"
        } else {
            userCorrente = User()
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
                        LoginScreen(auth, navController)
                    }
                    composable("FalesieScreen") {
                        FalesieScreen(navController)
                    }
                    composable("RegisterScreen") {
                        RegisterScreen(auth, navController)
                    }
//                    composable("screen1"){ entry ->
//                        val text = entry.savedStateHandle.get<String>("my_text")
//                        Column(modifier = Modifier.fillMaxSize()) {
//                            text?.let {
//                                Text(text = text)
//                            }
//                            Button(onClick = {navController.navigate("screen2")}) {
//                                Text(text = "Go to screen 2")
//                            }
//                        }
//                    }
//                    composable("screen2"){
//                        Column(modifier = Modifier.fillMaxSize()) {
//                            var text by remember{
//                                mutableStateOf("")
//                            }
//                            OutlinedTextField(
//                                value = text,
//                                onValueChange = { text = it},
//                                modifier = Modifier.width(300.dp)
//                            )
//                            Button(onClick = {
//                                navController.previousBackStackEntry
//                                    ?.savedStateHandle
//                                    ?.set("my_text", text)
//                                navController.popBackStack()
//                            }) {
//                                Text(text = "Apply")
//                            }
//                        }
//                    }

                }


//                Surface(color = MaterialTheme.colorScheme.background) {
//                    //LoginScreen(auth)
//                }
            }
        }

    }




}
