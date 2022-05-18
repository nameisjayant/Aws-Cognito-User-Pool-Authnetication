package com.codingwithjks.awscognito.repository

import android.util.Log
import com.amazonaws.mobileconnectors.cognitoidentityprovider.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.*
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import com.codingwithjks.awscognito.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val cognitoUserPool: CognitoUserPool
) {

    private var cognitoUser: CognitoUser? = null
    private var mForgetPassword:ForgotPasswordContinuation? = null

    fun registerUser(user: User) = callbackFlow {
        val userAttributes = CognitoUserAttributes()
        userAttributes.addAttribute("name", user.name)
        userAttributes.addAttribute("mobile_no", "+91${user.mobile.trim()}")
        userAttributes.addAttribute("email", user.email.trim())

        val signHandler = object : SignUpHandler {
            override fun onSuccess(user: CognitoUser?, signUpResult: SignUpResult?) {
                trySend("otp sent")
                cognitoUser = user
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

        awaitClose { }

    }


    fun verifyOtp(otp: String, user: User) = callbackFlow {

        val cognitoUser = cognitoUserPool.getUser(user.email.trim())

        val getDetailHandler = object : GetDetailsHandler {
            override fun onSuccess(cognitoUserDetails: CognitoUserDetails?) {
                Log.d(
                    "main",
                    "onSuccess: ${cognitoUserDetails?.attributes?.attributes?.get("sub")!!} "
                )
            }

            override fun onFailure(exception: Exception?) {
                trySend("${exception?.message}")
            }

        }

        val authHandler = object : AuthenticationHandler {
            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                Log.d("main", "onSuccess: ${userSession?.idToken?.jwtToken}")
                cognitoUser.getDetailsInBackground(getDetailHandler)
            }

            override fun getAuthenticationDetails(
                authenticationContinuation: AuthenticationContinuation?,
                userId: String?
            ) {
                val authDetail = AuthenticationDetails(
                    userId,
                    user.password.trim(),
                    null
                )
                authenticationContinuation?.setAuthenticationDetails(authDetail)
                authenticationContinuation?.continueTask()
            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                continuation?.continueTask()
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                continuation?.continueTask()
            }

            override fun onFailure(exception: Exception?) {
                trySend("${exception?.message}")
            }

        }

        val genricHandler = object : GenericHandler {
            override fun onSuccess() {
                trySend("otp verify")
                cognitoUser.getSessionInBackground(authHandler)
            }

            override fun onFailure(exception: Exception?) {
                trySend("${exception?.message}")
            }

        }

        cognitoUser.confirmSignUpInBackground(otp.trim(), false, genricHandler)

        awaitClose { }

    }


    fun resendOtp() = callbackFlow {

        val verificationHandler = object : VerificationHandler {
            override fun onSuccess(verificationCodeDeliveryMedium: CognitoUserCodeDeliveryDetails?) {
                trySend("otp sent again")
            }

            override fun onFailure(exception: Exception?) {
                trySend("${exception?.message}")
            }

        }

        cognitoUser?.resendConfirmationCodeInBackground(verificationHandler)

        awaitClose { }


    }


    fun login(user: User) = callbackFlow {

        val cognitoUser = cognitoUserPool.getUser(user.email.trim())

        val getDetailHandler = object : GetDetailsHandler {
            override fun onSuccess(cognitoUserDetails: CognitoUserDetails?) {
                Log.d(
                    "main",
                    "onSuccess: ${cognitoUserDetails?.attributes?.attributes?.get("sub")!!} "
                )
            }

            override fun onFailure(exception: Exception?) {
                trySend("${exception?.message}")
            }

        }

        val authHandler = object : AuthenticationHandler {
            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                Log.d("main", "onSuccess: ${userSession?.idToken?.jwtToken}")
                cognitoUser.getDetailsInBackground(getDetailHandler)
                trySend("Login Successful...!!")
            }

            override fun getAuthenticationDetails(
                authenticationContinuation: AuthenticationContinuation?,
                userId: String?
            ) {
                val authDetail = AuthenticationDetails(
                    userId,
                    user.password.trim(),
                    null
                )
                authenticationContinuation?.setAuthenticationDetails(authDetail)
                authenticationContinuation?.continueTask()
            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                continuation?.continueTask()
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                continuation?.continueTask()
            }

            override fun onFailure(exception: Exception?) {
                trySend("${exception?.message}")
            }

        }

        cognitoUser.getSessionInBackground(authHandler)
         awaitClose { }

    }


    fun forgetPassword(user:User) = callbackFlow {

        val cognitoUser = cognitoUserPool.getUser(user.email.trim())

        val forgetHandler = object : ForgotPasswordHandler{
            override fun onSuccess() {

            }

            override fun getResetCode(continuation: ForgotPasswordContinuation?) {
                 trySend("Otp Sent")
                mForgetPassword = continuation
            }

            override fun onFailure(exception: Exception?) {
                trySend("${exception?.message}")
            }

        }

        cognitoUser.forgotPasswordInBackground(forgetHandler)
         awaitClose { }

    }

    fun changeForgetPassword(otp: String,user: User) = callbackFlow {

        if(mForgetPassword != null){

            mForgetPassword?.apply {
                setVerificationCode(otp.trim())
                setPassword(user.password.trim())
                continueTask()
                trySend("password changed successfully..!!")
            }

        }else{
            trySend("something went Wrong")
        }

    }

 awaitClose { }
}
