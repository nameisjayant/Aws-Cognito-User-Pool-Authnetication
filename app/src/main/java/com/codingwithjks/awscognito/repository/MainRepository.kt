package com.codingwithjks.awscognito.repository

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import com.codingwithjks.awscognito.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import javax.inject.Inject

class MainRepository @Inject
constructor(private val cognitoUserPool: CognitoUserPool) {


    fun registerUser(user: User) = callbackFlow{
        val userAttributes = CognitoUserAttributes()
        userAttributes.addAttribute("name",user.name)
        userAttributes.addAttribute("mobile_no","+91${user.mobile.trim()}")
        userAttributes.addAttribute("email",user.email.trim())

        val signHandler = object : SignUpHandler{
            override fun onSuccess(user: CognitoUser?, signUpResult: SignUpResult?) {
                trySend("otp sent")
            }

            override fun onFailure(exception: Exception?) {
                trySend("${exception?.message}")
            }

        }

        cognitoUserPool.signUpInBackground(
            user.email.trim(),
            user.password.trim(),
            userAttributes,
            null,
            signHandler
        )

        awaitClose {  }

    }


}