package com.example.falesie.firestore

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.falesie.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(auth: FirebaseAuth) {

    // MODAL NAVIGATION DRAWER
    //https://www.youtube.com/watch?v=Fp-RB1lDkgo&t=75s
    // TOP APP BAR
    //https://www.youtube.com/watch?v=hQJpd78RUVg&list=PL-SHw8LBtGoZSwF_gS4FYbNybDftC9ssb&index=5&t=587s
    val scrollBehaivor = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val menuList = listOf(Icons.Filled.Contacts to "Contacts", Icons.Filled.History to "History")
    var selectedItems by remember { mutableStateOf(-1) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                menuList.forEachIndexed { index, data ->
                    NavigationDrawerItem(
                        modifier = Modifier.padding(top = 16.dp),
                        icon = {Icon(imageVector = data.first, contentDescription = data.second)},
                        label = {Text(text = data.second) },
                        selected = selectedItems == index,
                        onClick = {
                            selectedItems = index
                        })
                }

            }
        }
    ) {

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehaivor.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehaivor,
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu Icon"
                            )
                        }
                    },
                    title = {
                        Text(text = "Falesie")
                    },
//                     colors = TopAppBarDefaults.smallTopAppBarColors(
//                         containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
//                     )
                )
            },
            content = {
                //CustomList(paddingValues = it)
                LoginFrame(paddingValues = it, auth)
            }
        )


    }


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginFrame(paddingValues: PaddingValues, auth: FirebaseAuth) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isEmailValid by remember {
        derivedStateOf {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
    val isPasswordValid by remember { derivedStateOf { password.length > 5 } }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Bentornato in...",
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontSize = 32.sp,
            modifier = Modifier
                .padding(top = 20.dp, bottom = 16.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_task_image),
            contentDescription = "logo",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .fillMaxWidth()
        )

        Text(
            text = "...Falesie!",
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontSize = 40.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.secondary)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(all = 10.dp)
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Indirizzo Email") },
                    placeholder = { Text("abc@domain.com") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    isError = !isEmailValid,
                    trailingIcon = {
                        if (email.isNotBlank()) {
                            IconButton(onClick = { email = "" }) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "Clear email"
                                )
                            }
                        }
                    }
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.clearFocus() }
                    ),
                    isError = !isPasswordValid,
                    trailingIcon = {
                        IconButton(onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
                )

                Button(
                    onClick = {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Log.d(
                                        "Firebase",
                                        "The user has successfully loged in"
                                    )
                                    email = ""
                                    password = ""
                                    Toast.makeText(
                                        context,
                                        "The user has successfully loged in",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    //TODO AVVIARE LA SCHERMATA PRINCIPALE DELL'APPLICAZIONE
                                } else {
                                    Log.w(
                                        "Firebase",
                                        "The user has FAILED to log in",
                                        it.exception
                                    )
                                    email = ""
                                    password = ""
                                    Toast.makeText(
                                        context,
                                        "The user has FAILED to log in " +
                                                " ${it.exception.toString()}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.inversePrimary),
                    enabled = isEmailValid && isPasswordValid
                ) {
                    Text(
                        text = "Login",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    color = MaterialTheme.colorScheme.secondary,
                    fontStyle = FontStyle.Italic,
                    text = "Password dimenticata?",
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
            }
        }

        Button(
            onClick = {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("Firebase", "The user has successfully created")
                            email = ""
                            password = ""
                            Toast.makeText(
                                context,
                                "The user has successfully loged in",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Log.w(
                                "Firebase",
                                "The user has FAILED to create account",
                                it.exception
                            )
                            email = ""
                            password = ""
                            Toast.makeText(
                                context,
                                "The user has FAILED to create account " +
                                        " ${it.exception.toString()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            },
            enabled = isEmailValid && isPasswordValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            colors = ButtonDefaults.buttonColors(Color.White)
        ) {
            Text(
                text = "Registrati",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp
            )
        }

    }


}
