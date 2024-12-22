package com.example.myownchat.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myownchat.providers.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private val _selectedProvider = mutableStateOf<ChatProvider?>(null)
    val selectedProvider: ChatProvider? get() = _selectedProvider.value

    private val providers = listOf(
        ClaudeProvider(),
        GoogleProvider(),
        LocalLLMProvider()
    )
    val availableProviders: List<ChatProvider> get() = providers

    fun setApiKey(key: String) {
        selectedProvider?.let { provider ->
            if (provider.validateApiKey(key)) {
                _isApiKeySet.value = true
            }
        }
    }

    fun selectProvider(provider: ChatProvider) {
        _selectedProvider.value = provider
        _isApiKeySet.value = !provider.requiresApiKey()
    }

    fun sendMessage(message: String) {
        if (message.isBlank() || !isApiKeySet || selectedProvider == null) return
        
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

    private suspend fun getAIResponse(message: String): String {
        val conversationHistory = _messages.dropLast(1).map { msg ->
            Pair(if (msg.isUser) "user" else "assistant", msg.content)
        }

        return when (val result = selectedProvider!!.sendMessage(message, conversationHistory)) {
            is com.example.myownchat.data.Result.Success -> result.data
            is com.example.myownchat.data.Result.Error -> throw result.exception
        }
    }

    fun clearMessages() {
        _messages.clear()
    }

    fun updateLocalLLMUrl(url: String) {
        providers.filterIsInstance<LocalLLMProvider>().firstOrNull()?.updateServerUrl(url)
    }
}
