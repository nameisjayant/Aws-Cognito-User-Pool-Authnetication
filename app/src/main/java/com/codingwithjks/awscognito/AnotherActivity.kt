package com.codingwithjks.awscognito


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codingwithjks.awscognito.model.User
import com.codingwithjks.awscognito.ui.theme.AwsCognitoTheme
import com.codingwithjks.awscognito.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnotherActivity : ComponentActivity() {

    private val viewModel:MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AwsCognitoTheme() {
                var email by remember { mutableStateOf("") }
                val scope = rememberCoroutineScope()
                var isDialog by remember { mutableStateOf(false) }
                var loginPassword by remember { mutableStateOf("") }
                var otp by remember { mutableStateOf("") }
                Surface() {
                    LazyColumn {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 30.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(text = "Forget Password", fontWeight = FontWeight.Bold)
                            }
                        }

                        item {
                            Column() {
                                OutlinedTextField(
                                    value = email, onValueChange = {
                                        email = it
                                    },
                                    label = { Text(text = "Email") },
                                    placeholder = { Text(text = "Enter name") },
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
                                    scope.launch(Dispatchers.Main){
                                        viewModel.forgetPassword(
                                            User(email = email)
                                        ).collect{
                                            isDialog = false
                                            Toast.makeText(this@AnotherActivity, it, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = "Get Otp",
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 30.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(text = "Change Password", fontWeight = FontWeight.Bold)
                            }
                        }

                        item {
                            OutlinedTextField(
                                value = otp, onValueChange = {
                                    otp = it
                                },
                                placeholder = { Text(text = "Enter otp") },
                                label = { Text(text = "Otp") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                        }


                        item {
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
                        }

                        item {
                            Button(
                                onClick = {
                                          isDialog = true
                                   scope.launch(Dispatchers.Main){
                                       viewModel.changeForgetPassword(
                                           User(password =  loginPassword),
                                           otp
                                       ).collect{
                                           isDialog = false
                                           Toast.makeText(this@AnotherActivity, it, Toast.LENGTH_SHORT).show()
                                       }
                                   }
                                }, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = "Done",
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
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
            }
        }
    }
}