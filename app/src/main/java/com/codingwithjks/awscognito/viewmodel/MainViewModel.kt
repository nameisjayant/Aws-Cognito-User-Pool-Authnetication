package com.codingwithjks.awscognito.viewmodel

import androidx.lifecycle.ViewModel
import com.codingwithjks.awscognito.model.User
import com.codingwithjks.awscognito.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo:MainRepository) : ViewModel()  {

    fun registerUser(user: User) = repo.registerUser(user)

    fun verifyOtp(user: User,otp:String) = repo.verifyOtp(otp,user)

    val resendOtp = repo.resendOtp()

    fun login(user: User)  = repo.login(user)

    fun forgetPassword(user: User) = repo.forgetPassword(user)

    fun changeForgetPassword(user: User,otp: String) = repo.changeForgetPassword(otp,user)

    fun resetPassword(user: User,oldPassword:String,newPassword:String) = repo.resetPassword(user,oldPassword,newPassword)
}