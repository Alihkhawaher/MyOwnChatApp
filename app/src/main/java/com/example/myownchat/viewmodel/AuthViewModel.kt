package com.example.myownchat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myownchat.FIREBASE_AUTH
import com.example.myownchat.data.Injection
import com.example.myownchat.data.Result
import com.example.myownchat.data.User
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
    private val _userFromFirebasePairUser = MutableLiveData<User>()

    val authResult: LiveData<Result<Boolean>> get() = _authResult
    val userFromFirebasePairUser: LiveData<User> get() = _userFromFirebasePairUser

    fun signUp(email: String, password: String, login: String, image: String){
        viewModelScope.launch {
            _authResult.value = userRepository.signUp(email, password, login, image)
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


    fun getUser(){
        viewModelScope.launch {
            val userGetCurrentUserPair = userRepository.getCurrentUser()
            val userGetCurrentUserPairResult = userGetCurrentUserPair.first
            when(userGetCurrentUserPairResult){
                is Result.Error -> _userFromFirebasePairUser.value = userGetCurrentUserPair.second
                is Result.Success -> _userFromFirebasePairUser.value = userGetCurrentUserPair.second
            }
        }
    }
}