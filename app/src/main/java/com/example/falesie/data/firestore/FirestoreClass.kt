package com.example.falesie.data.firestore

//import com.example.falesie.screen.VieViewModel
//import com.example.falesie.screen.VieViewModelFactory



import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.example.falesie.Aggiorna
import com.example.falesie.Constants
import com.example.falesie.MainActivity.Companion.arrayVieScalateUser
import com.example.falesie.MainActivity.Companion.listaFalesie
import com.example.falesie.MainActivity.Companion.listaVie
import com.example.falesie.MainActivity.Companion.userCorrente
import com.example.falesie.R
import com.example.falesie.data.firestore.model.Falesia
import com.example.falesie.data.firestore.model.User
import com.example.falesie.data.firestore.model.Via
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.IOException
import java.io.InputStream


class FirestoreClass{
    //var context = LocalContext
    // Create a instance of Firebase Firestore
    private val mFireStore = FirebaseFirestore.getInstance()
    //var listaVie2: ArrayList<Via> = ArrayList()
    val listaVie2 = MutableLiveData<List<Via>>()

    //VARIABILI PER STORAGE
    private val mFireStoreStorage = FirebaseStorage.getInstance()

//    // Create a storage reference from our app
//    private val storageRef = mFireStoreStorage.reference

//    // Create a reference to "mountains.jpg"
//    val mountainsRef = storageRef.child("mountains.jpg")

//    // Create a reference to 'images/mountains.jpg'
//    val mountainImagesRef = storageRef.child("images/mountains.jpg")

//// While the file names are the same, the references point to different files
//    mountainsRef.name == mountainImagesRef.name // true
//    mountainsRef.path == mountainImagesRef.path // false

    // Get the data from an ImageView as bytes
//    imageView.isDrawingCacheEnabled = true
//    imageView.buildDrawingCache()


    fun updateVia(via: Via, context: Context) {
        //val idAutogenerato = mFireStore.collection("vie").document().id
        //via.id = idAutogenerato
        mFireStore.collection("vie")
            .document(via.id)
            .set(via, SetOptions.merge())
            .addOnCompleteListener{
                Log.e("firebase", "Via AGGIORNATA correttamente.")
                Toast.makeText(context, "Via AGGIORNATA correttamente.", Toast.LENGTH_SHORT).show()
            }
    }



    fun updateFalesia(falesia: Falesia, context: Context) {
        //val idAutogenerato = mFireStore.collection("vie").document().id
        //via.id = idAutogenerato
        mFireStore.collection("falesie")
            .document(falesia.id)
            .set(falesia, SetOptions.merge())
            .addOnCompleteListener{
                Log.e("firebase", "Falesia AGGIORNATA correttamente.")
                Toast.makeText(context, "Falesia AGGIORNATA correttamente.", Toast.LENGTH_SHORT).show()
            }
    }






    fun creaVia(via: Via,context: Context) {
        val idAutogenerato = mFireStore.collection("vie").document().id
        via.id = idAutogenerato
        mFireStore.collection("vie")
            .document(via.id)
            .set(via, SetOptions.merge())
            .addOnCompleteListener{
                Log.e("firebase", "Via creata correttamente.")
                Toast.makeText(context, "Via creata correttamente.", Toast.LENGTH_SHORT).show()
            }
    }

    fun creaFalesia(falesia: Falesia,context: Context) {
        val idAutogenerato = mFireStore.collection("falesie").document().id
        falesia.id = idAutogenerato
        mFireStore.collection("falesie")
            .document(falesia.id)
            .set(falesia, SetOptions.merge())
            .addOnCompleteListener{
                Log.e("firebase", "Falesia creata correttamente.")
                Toast.makeText(context, "Falesia creata correttamente.", Toast.LENGTH_SHORT).show()
            }
    }






    fun putImageInStorage(context: Context, resourceDrawable: Int ){    // Create a storage reference from our app
        val nomeImmagine =  context.resources.getResourceEntryName(resourceDrawable)

        val linkUri = Uri.parse("android.resource://" + context.packageName + "/" + resourceDrawable)
        Log.d("LINK URI",linkUri.toString())
        val iStream: InputStream? = context.contentResolver.openInputStream(linkUri)

        val uploadTask = mFireStoreStorage.reference
            .child(nomeImmagine + ".webp")
            .putFile(linkUri)

        uploadTask.addOnFailureListener {
            Log.d("IMMAGINE","Caricamento Fallito")
        }.addOnSuccessListener { taskSnapshot ->
            Log.d("IMMAGINE","Caricamento Avvenuto")
        }
    }

//    fun getImageFromStorage(context: Context, nomeImmagine: String){
//        val myDir: File = File(context.filesDir, "falesie")         //DIRECTORY SALVATAGGIO IMMAGINI
//        if (!myDir.exists()){
//            myDir.mkdir()
//            //Log.d("CREO", myDir.toString())
//        }
//        //Log.d("ESISTE GIA'", myDir.toString())
//
//        val new_file: File = File("$myDir" + File.separator + nomeImmagine +".webp")
//        try {
//            new_file.createNewFile()
//        } catch (e: IOException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        }
//        Log.d("Create File", "File exists?" + new_file.exists())
//
//
// //SALVA FILE IN LOCALE
//        val islandRef = mFireStoreStorage.reference.child("$nomeImmagine.webp")
//        islandRef.getFile(new_file).addOnSuccessListener {
//                Log.d("file salvato", islandRef.name)
//            }.addOnFailureListener {
//                Log.d("ERRORE scaricamento", islandRef.name)
//            }
//
//
//    }

    fun getAllImageFromStorage(context: Context){
        val myDir: File = File(context.filesDir, "falesie")         //DIRECTORY SALVATAGGIO IMMAGINI
        if (!myDir.exists()){
            myDir.mkdir()
            //Log.d("CREO", myDir.toString())
        }
        //Log.d("ESISTE GIA'", myDir.toString())

        var listaNomi = mFireStoreStorage.reference.listAll()

        listaNomi.addOnSuccessListener {
            listaNomi.result.items.forEach {
                //Log.d("CREO", it.name)

                val new_file: File = File("$myDir" + File.separator + it.name)
                try {
                    new_file.createNewFile()
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
                //Log.d("Create File", "File exists?" + new_file.exists())


                Log.d("Percorso file", new_file.toString())
                val islandRef = mFireStoreStorage.reference.child(it.name)
                islandRef.getFile(new_file).addOnSuccessListener {
                    Log.d("file salvato", islandRef.name)
                }.addOnFailureListener {
                    Log.d("ERRORE scaricamento", islandRef.name)
                }
            }
        }
    }


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
                    val loggedInUser = document.toObject(User::class.java)

                loggedInUser?.let { it2 ->
                    userCorrente = loggedInUser
                }

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

    fun updateUserProfileData(userHashMap: HashMap<String, Any>) {

        Log.d("FIRESTORE", userHashMap.toString())

        mFireStore.collection(Constants.USERS) // Collection Name
            .document(getCurrentUserID()) // Document ID
            .update(userHashMap) // A hashmap of fields which are to be updated.
            .addOnSuccessListener {
                // Profile data is updated successfully.
                Log.d("FIRESTORE", "Profile Data updated successfully!")
            }
            .addOnFailureListener { e ->
                //activity.hideProgressDialog()
                Log.e(
                    "FIRESTORE",
                    "Error while creating a user profile.",
                    e
                )
            }
    }

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

    fun readAllFalesie(aggiorna: Aggiorna) {
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

    fun readAllVie(aggiorna: Aggiorna) {
        mFireStore.collection(Constants.VIA).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //val list = ArrayList<Via>()
                    listaVie.clear()
                    for (document in task.result) {
                        Log.i("*************Vie lette de Firestore", "${document.id}")
                        val via = document.toObject(Via::class.java)
                        listaVie.add(via)

                        listaVie2.value = listaVie2.value?.plus(via) ?: listOf(via)

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


