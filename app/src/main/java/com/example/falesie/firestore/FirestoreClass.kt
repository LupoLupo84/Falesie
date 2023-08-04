package com.example.falesie.firestore

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.falesie.Aggiorna
import com.example.falesie.Constants
import com.example.falesie.MainActivity.Companion.arrayVieScalateUser
import com.example.falesie.MainActivity.Companion.listaFalesie
import com.example.falesie.MainActivity.Companion.listaVie
import com.example.falesie.MainActivity.Companion.userCorrente
import com.example.falesie.R
import com.example.falesie.model.Falesia
import com.example.falesie.model.User
import com.example.falesie.model.Via
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit


class FirestoreClass {
    // Create a instance of Firebase Firestore
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(context: Context, userInfo: User, navController: NavHostController) {

        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(getCurrentUserID())
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                //activity.userRegisteredSuccess()
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.account_creato),
                    Toast.LENGTH_LONG
                ).show()

                FirebaseAuth.getInstance().signOut()
                //TODO inserire ritorno ad applicazione
                navController.popBackStack()
                navController.navigate("LoginScreen")

            }
            .addOnFailureListener { e ->
                //activity.hideProgressDialog()
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.errore_account),
                    Toast.LENGTH_LONG
                ).show()


                Log.e(
                    "ERRORE",
                    "Error writing document",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun loadUserData(provenienza: String, navController: NavHostController, context: Context) {


        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.d("LETTURA DOCUMENTO", document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val loggedInUser = document.toObject(User::class.java)!!
                userCorrente = loggedInUser

                if (provenienza == "LoginScreen") {
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.login_eseguito) + " " + userCorrente.nome + "!!",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.popBackStack()
                    navController.navigate("FalesieScreen")
                }

            }
            .addOnFailureListener { e ->
                Log.e(
                    "ERRORE",
                    "Error while getting loggedIn user details",
                    e
                )
            }
    }


    //fun firstLoadUserData(aggiorna: Aggiorna) {
    fun firstLoadUserData(): User {

        lateinit var userRitorno: User
        //userCorrente = User()
        Log.d("TEST", "ingresso nella funzione")
        Log.d("TEST", getCurrentUserID())
        if (getCurrentUserID().isEmpty()) {
            Log.d("TEST", "IS EMPTY")
            userCorrente = User()
            userRitorno = User()
        } else
        // Here we pass the collection name from which we wants the data.
        {

            runBlocking {
                var lettura = mFireStore.collection("users")
                    .document(getCurrentUserID())
                    .get()

                var let = lettura.await()
                userRitorno = let.toObject(User::class.java)!!

//                mFireStore.collection("users")
//                    // The document id to get the Fields of user.
//                    .document(getCurrentUserID())
//                    .get()
//                    .addOnCompleteListener { task ->
//                        Log.d("TEST", "task completato")
//                        if (task.isSuccessful) {
//                            Log.d("TEST", "task CORRETTO")
//                            Log.d("TEST", task.result.toString())
//                            //var testUser = task.result.toObject(User::class.java)
//                            userRitorno = task.result.toObject(User::class.java)!!
//                            //Log.d("TEST", "${testUser?.email.toString()}")
//                        } else {
//                            Log.d("TEST", "errore chiamata")
//                        }
//                    }
//                    .addOnFailureListener {
//                        Log.d("TEST", "on failure")
//
//                    }
            }
        }
        Log.d("TEST", "task SALTATO")
        return userRitorno
    }


    fun leggiTutteLeFalesie(aggiorna: Aggiorna) {
        mFireStore.collection(Constants.FALESIA).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //val list = ArrayList<Via>()
                    for (document in task.result) {
                        val falesia = document.toObject(Falesia::class.java)
                        //Log.i("*************Falesia letta da Firestore", "${falesia.id} -> ${falesia.nome}")
                        listaFalesie.add(falesia)
                        //Log.i("*************Vie lette de Firestore", "${list.size}")
                    }
                    aggiorna.aggiorna()
                }
            }
            .addOnFailureListener { e ->
                //BaseActivity().showErrorSnackBar(e.toString())
                Log.e("ERRORE", "${e}")
            }
    }


    fun leggiTutteLeVie(aggiorna: Aggiorna) {
        mFireStore.collection(Constants.VIA).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //val list = ArrayList<Via>()
                    listaVie.clear()
                    for (document in task.result) {
                        Log.i("*************Vie lette de Firestore", "${document.id}")
                        val via = document.toObject(Via::class.java)
                        listaVie.add(via)
                        //Log.i("*************Vie lette de Firestore", "${list.size}")
                    }
                    aggiorna.aggiorna()
                }
            }
            .addOnFailureListener { e ->
                //BaseActivity().showErrorSnackBar(e.toString())
                Log.e("ERRORE", "${e}")
            }
    }


    //fun leggiVieScalateUser(userId: String, aggiorna: Aggiorna) {
    fun leggiVieScalateUser(userId: String) {
        mFireStore.collection(Constants.USERS)
            .document(userId)
            .get()
            .addOnCompleteListener { task ->
                Log.d("TEST", "task completato")
                if (task.isSuccessful) {
                    Log.d("TEST", "task CORRETTO")

                    val userLetto = task.result.toObject(User::class.java)!!
                    arrayVieScalateUser = ArrayList(userLetto.vieScalate)

                } else {
                    Log.d("TEST", "errore chiamata")
                }
            }


    }


}


