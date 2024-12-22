package com.example.myownchat.providers

import com.example.myownchat.data.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class LocalLLMProvider(private var serverUrl: String = "http://localhost:8080") : ChatProvider {
    private val client = OkHttpClient()
    private val JSON = "application/json; charset=utf-8".toMediaType()

    override suspend fun sendMessage(
        message: String,
        conversationHistory: List<Pair<String, String>>
    ): Result<String> {
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
                put("messages", messages)
                put("stream", false)
                put("max_tokens", 1000)
                put("temperature", 0.7)
            }

            val request = Request.Builder()
                .url("$serverUrl/v1/chat/completions")
                .post(requestBody.toString().toRequestBody(JSON))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return Result.Error(IOException("Local LLM API call failed: ${response.code}"))
                }

                val responseBody = response.body?.string() ?: return Result.Error(IOException("Empty response"))
                val jsonResponse = JSONObject(responseBody)
                return Result.Success(
                    jsonResponse
                        .getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                )
            }
        } catch (e: IOException) {
            return Result.Error(e)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override fun requiresApiKey(): Boolean = false

    override fun getName(): String = "Local LLM"

    override fun getDescription(): String = "Local Language Model - Running on your machine"

    override fun validateApiKey(apiKey: String): Boolean = true

    fun updateServerUrl(url: String) {
        serverUrl = url
    }
}
