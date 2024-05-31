package com.example.myownchat.repository

import android.util.Log
import android.widget.Toast
import com.example.myownchat.data.Result
import com.example.myownchat.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth?,
    private val store: FirebaseFirestore
) {

    suspend fun signUp(
        email: String,
        password: String,
        login: String,
    ): Result<Boolean>{
        return try {
            auth?.createUserWithEmailAndPassword(email, password)?.await()
            val user = User(email = email, login =  login)
            saveUserToFirestoreDatabase(user)
            Result.Success(true)
        }catch(e: Exception){
            Result.Error(e)
        }
    }

    suspend fun signIn(
        email: String,
        password: String
    ): Result<Boolean>{
        return try{
            auth?.signInWithEmailAndPassword(email, password)?.await()
            Result.Success(true)
        }catch(e: Exception){
            Result.Error(e)
        }
    }

    fun signOut(): Result<Boolean>{
        return try {
            auth?.signOut()
            Result.Success(true)
        }catch (e: Exception){
            Result.Error(e)
        }
    }

    private suspend fun saveUserToFirestoreDatabase(user: User){
        store.collection("users").document(user.email).set(user).await()
    }

    suspend fun getCurrentUser(): Result<User> = try {
        val uid = auth?.currentUser?.uid
        if (uid != null){
            val userDocument = store.collection("users").document(uid).get().await()
            val user = userDocument.toObject(User::class.java)
            if (user != null){
                Log.d("getCurrentUser", "$uid")
                Result.Success(user)
            } else{
                Result.Error(Exception("User data not found"))
            }
        }
        else{
            Result.Error(Exception("User not authenticated"))
        }
    } catch (e: Exception){
        Result.Error(e)
    }
}