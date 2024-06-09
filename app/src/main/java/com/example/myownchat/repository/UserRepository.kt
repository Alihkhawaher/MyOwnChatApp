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
        image: String
   ): Result<Boolean>{
        return try {
            auth?.createUserWithEmailAndPassword(email, password)?.await()
            val user = User(email = email, login = login, image = image)
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

    suspend fun getCurrentUser(): Pair<Result<User>,User?> {
        return try {
            val umail = auth?.currentUser?.email
            if (umail != null){
                val userDocument = store.collection("users").document(umail).get().await()
                val user = userDocument.toObject(User::class.java)
                if (user != null){
                    Log.d("current user", user.toString())
                    Pair(Result.Success(user),user)
                } else{
                    Pair(Result.Error(Exception("User data not found")),null)
                }
            }
            else{
                Pair(Result.Error(Exception("User not authenticated")),null)
            }
        } catch (e: Exception){
            Pair(Result.Error(e),null)
        }
    }
}