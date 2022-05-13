package com.codingwithjks.awscognito.viewmodel

import androidx.lifecycle.ViewModel
import com.codingwithjks.awscognito.model.User
import com.codingwithjks.awscognito.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo:MainRepository) : ViewModel()  {

    fun registerUser(user: User) = repo.registerUser(user)
}