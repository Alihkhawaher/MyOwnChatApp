package com.example.myownchat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myownchat.FIREBASE_AUTH
import com.example.myownchat.data.Injection
import com.example.myownchat.data.Result
import com.example.myownchat.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    val userRepository: UserRepository
    init {
        userRepository = UserRepository(FIREBASE_AUTH, Injection.instance())
    }

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    fun signUp(email: String, password: String, login: String){
        viewModelScope.launch {
            _authResult.value = userRepository.signUp(email, password, login)
        }
    }

    fun login(email: String, password: String){
        viewModelScope.launch {
            _authResult.value = userRepository.signIn(email, password)
        }
    }

    fun signOut(){
        _authResult.value = userRepository.signOut()
    }
}