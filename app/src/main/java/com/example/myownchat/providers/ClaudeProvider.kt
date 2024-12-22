package com.example.myownchat.providers

import com.example.myownchat.data.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ClaudeProvider(private var apiKey: String = "") : ChatProvider {
    private val client = OkHttpClient()
    private val JSON = "application/json; charset=utf-8".toMediaType()
    private val BASE_URL = "https://api.anthropic.com/v1/messages"

    override suspend fun sendMessage(
        message: String,
        conversationHistory: List<Pair<String, String>>
    ): Result<String> {
        if (apiKey.isEmpty()) {
            return Result.Error(IllegalStateException("Claude API key not set"))
        }

        try {
            val messages = JSONArray()
            conversationHistory.forEach { (role, content) ->
                messages.put(JSONObject().apply {
                    put("role", if (role == "user") "user" else "assistant")
                    put("content", content)
                })
            }
            messages.put(JSONObject().apply {
                put("role", "user")
                put("content", message)
            })

            val requestBody = JSONObject().apply {
                put("model", "claude-3-opus-20240229")
                put("messages", messages)
                put("max_tokens", 1000)
            }

            val request = Request.Builder()
                .url(BASE_URL)
                .addHeader("x-api-key", apiKey)
                .addHeader("anthropic-version", "2023-06-01")
                .post(requestBody.toString().toRequestBody(JSON))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return Result.Error(IOException("API call failed: ${response.code}"))
                }

                val responseBody = response.body?.string() ?: return Result.Error(IOException("Empty response"))
                val jsonResponse = JSONObject(responseBody)
                return Result.Success(jsonResponse.getJSONObject("content").getString("text"))
            }
        } catch (e: IOException) {
            return Result.Error(e)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override fun requiresApiKey(): Boolean = true

    override fun getName(): String = "Claude"

    override fun getDescription(): String = "Anthropic's Claude - Advanced AI assistant"

    override fun validateApiKey(apiKey: String): Boolean {
        this.apiKey = apiKey
        return apiKey.isNotEmpty()
    }
}
