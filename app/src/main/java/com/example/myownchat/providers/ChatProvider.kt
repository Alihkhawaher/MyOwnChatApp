package com.example.myownchat.providers

import com.example.myownchat.data.Result

interface ChatProvider {
    suspend fun sendMessage(
        message: String,
        conversationHistory: List<Pair<String, String>>
    ): Result<String>
    
    fun requiresApiKey(): Boolean
    
    fun getName(): String
    
    fun getDescription(): String
    
    fun validateApiKey(apiKey: String): Boolean
}
