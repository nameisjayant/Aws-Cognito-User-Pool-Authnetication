package com.codingwithjks.awscognito.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingwithjks.awscognito.model.User
import com.codingwithjks.awscognito.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    viewModel: MainViewModel
) {
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var isDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LazyColumn {

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "REGISTER", fontWeight = FontWeight.Bold)
            }
        }

        item {
            Column() {
                OutlinedTextField(
                    value = name, onValueChange = {
                        name = it
                    },
                    label = { Text(text = "Name") },
                    placeholder = { Text(text = "Enter name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )

                OutlinedTextField(
                    value = mobile, onValueChange = {
                        mobile = it
                    },
                    placeholder = { Text(text = "Enter mobile") },
                    label = { Text(text = "Mobile") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )

                OutlinedTextField(
                    value = email, onValueChange = {
                        email = it
                    },
                    placeholder = { Text(text = "Enter email") },
                    label = { Text(text = "Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )

                OutlinedTextField(
                    value = password, onValueChange = {
                        password = it
                    },
                    placeholder = { Text(text = "Enter password") },
                    label = { Text(text = "Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }
        }

        item {
            Button(
                onClick = {
                    isDialog = true
                    scope.launch(Dispatchers.Main) {
                        viewModel.registerUser(
                            User(
                                name,
                                email,
                                mobile,
                                password
                            )
                        ).collect {
                            isDialog = false
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(text = "Register", modifier = Modifier.padding(vertical = 10.dp))
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "OTP", fontWeight = FontWeight.Bold)
            }
        }

        item {
            Column() {
                OutlinedTextField(
                    value = otp, onValueChange = {
                        otp = it
                    },
                    placeholder = { Text(text = "Enter otp") },
                    label = { Text(text = "OTP") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    ClickableText(
                        text = buildAnnotatedString { append("Resend Otp") },
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color.Gray,
                        )
                    ) {
                        isDialog = true
                        scope.launch(Dispatchers.Main) {
                            viewModel.resendOtp.collect {
                                isDialog = false
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    isDialog = true
                    scope.launch(Dispatchers.Main) {
                        viewModel.verifyOtp(
                            User(email = email, password = password),
                            otp
                        ).collect {
                            isDialog = false
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(text = "Verify Otp", modifier = Modifier.padding(vertical = 10.dp))
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Login", fontWeight = FontWeight.Bold)
            }
        }

        item {
            Column() {
                OutlinedTextField(
                    value = loginEmail, onValueChange = {
                        loginEmail = it
                    },
                    placeholder = { Text(text = "Enter email") },
                    label = { Text(text = "Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )

                OutlinedTextField(
                    value = loginPassword, onValueChange = {
                        loginPassword = it
                    },
                    placeholder = { Text(text = "Enter password") },
                    label = { Text(text = "Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                ) {
                    ClickableText(
                        text = buildAnnotatedString { append("Forget Password") },
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color.Gray,
                        )
                    ) {

                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(text = "Login", modifier = Modifier.padding(vertical = 10.dp))
            }
        }

    }

    if (isDialog) {
        AlertDialog(
            onDismissRequest = { },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            backgroundColor = Color.Transparent
        )
    }
}