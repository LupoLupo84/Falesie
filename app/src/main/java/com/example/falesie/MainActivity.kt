package com.example.falesie

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.falesie.firestore.LoginScreen
import com.example.falesie.ui.theme.FalesieTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern



class MainActivity : ComponentActivity() {

    private val auth by lazy {
        Firebase.auth
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FalesieTheme() {
                Surface(color = MaterialTheme.colorScheme.background) {
                    LoginScreen(auth)
                }
            }
        }

    }



}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LoginScreen(){
//
//    val focusManager = LocalFocusManager.current
//
//    var email by remember {
//        mutableStateOf("")
//    }
//
//    var password by remember {
//        mutableStateOf("")
//    }
//
//
//    val isEmailValid by remember{
//        derivedStateOf{
//        Patterns.EMAIL_ADDRESS.matcher(email).matches()
//        }
//    }
//
//    val isPasswordValid by remember {
//        derivedStateOf {
//           password.length > 7
//        }
//    }
//
//    var isPasswordVisible by remember {
//        mutableStateOf( false)
//    }
//
//
//    Column(
//        modifier = Modifier
//            .background(color = Color.LightGray)
//            .fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Top
//    ) {
//        Text(
//            text = "Welcome back...",
//            fontFamily = FontFamily.Companion.SansSerif,
//            fontWeight = FontWeight.Bold,
//            fontStyle = FontStyle.Italic,
//            fontSize = 32.sp,
//            modifier = Modifier
//                .padding(top = 20.dp)
//        )
//
//        Image(
//            painter = painterResource(id = R.drawable.ic_task_image),
//            contentDescription = "logo",
//            modifier = Modifier
//                .fillMaxWidth()
//        )
//
//        Text(
//            text = "...to the house of the fried chicken",
//            fontFamily = FontFamily.Companion.SansSerif,
//            fontWeight = FontWeight.Bold,
//            fontStyle = FontStyle.Italic,
//            fontSize = 20.sp,
//            modifier = Modifier
//                .padding(bottom = 16.dp)
//        )
//
//
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 8.dp),
//            shape = RoundedCornerShape(16.dp),
//            border = BorderStroke(1.dp, color = Color.Black)
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.spacedBy(8.dp),
//                    modifier = Modifier
//                        .padding(all = 10.dp)
//                ) {
//                    OutlinedTextField(
//                        value = email,
//                        onValueChange = {email = it},
//                        label = { Text("Email Address") },
//                        placeholder = {Text("abc@domain.com")},
//                        singleLine = true,
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        keyboardOptions = KeyboardOptions(
//                            keyboardType = KeyboardType.Email,
//                            imeAction = ImeAction.Next
//                        ),
//                        keyboardActions = KeyboardActions(
//                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
//                        ),
//                        isError = !isEmailValid,
//                        trailingIcon = {
//                            if (email.isNotBlank()){
//                                IconButton(onClick = {email = ""}) {
//                                    Icon(
//                                        imageVector = Icons.Filled.Clear,
//                                        contentDescription = "Clear email"
//                                    )
//                                }
//                            }
//                        }
//                    )
//
//                    OutlinedTextField(
//                        value = password,
//                        onValueChange = {password = it},
//                        label = { Text("Password") },
//                        singleLine = true,
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        keyboardOptions = KeyboardOptions(
//                            keyboardType = KeyboardType.Password,
//                            imeAction = ImeAction.Done
//                        ),
//                        keyboardActions = KeyboardActions(
//                            onNext = { focusManager.clearFocus() }
//                        ),
//                        isError = !isPasswordValid,
//                        trailingIcon = {
//                            IconButton(onClick = {isPasswordVisible = !isPasswordVisible}) {
//                                Icon(
//                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                                    contentDescription = "Toggle password visibility"
//                                )
//                            }
//                        },
//                        visualTransformation = if (isPasswordVisible)  VisualTransformation.None else PasswordVisualTransformation()
//                    )
//
//                    Button(onClick = {  },
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        colors = ButtonDefaults.buttonColors(Color.Green),
//                        enabled = isEmailValid && isPasswordValid
//                        ) {
//                        Text(
//                            text = "Login",
//                            fontWeight = FontWeight.Bold,
//                            color = Color.Black,
//                            fontSize = 16.sp
//                        )
//                    }
//                }
//        }
//        Row(
//            horizontalArrangement = Arrangement.End,
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//            TextButton(onClick = { /*TODO*/ }) {
//                Text(
//                    color = Color.Black,
//                    fontStyle = FontStyle.Italic,
//                    text = "Frogotten password?",
//                    modifier = Modifier
//                        .padding(end = 8.dp)
//                )
//            }
//        }
//
//        Button(
//            onClick =  {},
//            enabled = true,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(all = 16.dp),
//            colors = ButtonDefaults.buttonColors(Color.White)
//        ) {
//            Text(
//                text = "Register",
//                fontWeight = FontWeight.Bold,
//                color = Color.Black,
//                fontSize = 16.sp
//            )
//        }
//
//    }
//
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview(){
//    FalesieTheme{
//        LoginScreen()
//    }
//}