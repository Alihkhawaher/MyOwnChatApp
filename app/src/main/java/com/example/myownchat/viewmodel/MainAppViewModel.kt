package com.example.myownchat.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

data class Message(
    val content: String,
    val isUser: Boolean
)

class MainAppViewModel : ViewModel() {
    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> get() = _messages

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    private val _isApiKeySet = mutableStateOf(false)
    val isApiKeySet: Boolean get() = _isApiKeySet.value

    private var apiKey: String = ""
    private val client = OkHttpClient()

    fun setApiKey(key: String) {
        apiKey = key
        _isApiKeySet.value = true
    }

    fun sendMessage(message: String) {
        if (message.isBlank() || !isApiKeySet) return
        
        _messages.add(Message(message, true))
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    getAIResponse(message)
                }
                _messages.add(Message(response, false))
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error getting AI response", e)
                _messages.add(Message("Sorry, I encountered an error. Please try again.", false))
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getAIResponse(message: String): String {
        val messagesArray = JSONArray().apply {
            // Add all previous messages for context
            _messages.forEach { msg ->
                put(JSONObject().apply {
                    put("role", if (msg.isUser) "user" else "assistant")
                    put("content", msg.content)
                })
            }
            // Add the new message
            put(JSONObject().apply {
                put("role", "user")
                put("content", message)
            })
        }

        val jsonBody = JSONObject().apply {
            put("model", "gpt-3.5-turbo")
            put("messages", messagesArray)
            put("temperature", 0.7)
        }

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                val errorBody = response.body?.string()
                Log.e("ChatViewModel", "API Error: $errorBody")
                throw IOException("API error: ${response.code}")
            }
            
            val responseBody = response.body?.string() ?: throw IOException("Empty response")
            val jsonResponse = JSONObject(responseBody)
            return jsonResponse.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
        }
    }

    fun clearMessages() {
        _messages.clear()
    }
}
