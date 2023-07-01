package com.example.falesie.firestore

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.falesie.Aggiorna
import com.example.falesie.Constants
import com.example.falesie.MainActivity.Companion.listaFalesie
import com.example.falesie.MainActivity.Companion.listaVie
import com.example.falesie.MainActivity.Companion.userCorrente
import com.example.falesie.R
import com.example.falesie.model.Falesia
import com.example.falesie.model.User
import com.example.falesie.model.Via
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


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


    fun firstLoadUserData() {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.d("LETTURA DOCUMENTO", document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                //val loggedInUser = document.toObject(User::class.java)!!
                //userCorrente = loggedInUser
                userCorrente = document.toObject(User::class.java)!!

            }
            .addOnFailureListener { e ->
                Log.e(
                    "ERRORE",
                    "Error while getting loggedIn user details",
                    e
                )
            }
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

}


