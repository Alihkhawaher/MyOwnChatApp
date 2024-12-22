package com.example.myownchat.providers

import com.example.myownchat.data.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class GoogleProvider(private var apiKey: String = "") : ChatProvider {
    private val client = OkHttpClient()
    private val JSON = "application/json; charset=utf-8".toMediaType()
    private val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent"

    override suspend fun sendMessage(
        message: String,
        conversationHistory: List<Pair<String, String>>
    ): Result<String> {
        if (apiKey.isEmpty()) {
            return Result.Error(IllegalStateException("Google API key not set"))
        }

        try {
            val contents = JSONArray()
            conversationHistory.forEach { (role, content) ->
                contents.put(JSONObject().apply {
                    put("role", if (role == "user") "user" else "model")
                    put("parts", JSONArray().put(JSONObject().put("text", content)))
                })
            }
            contents.put(JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", message)))
            })

            val requestBody = JSONObject().apply {
                put("contents", contents)
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                    put("maxOutputTokens", 1000)
                })
            }

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody.toString().toRequestBody(JSON))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return Result.Error(IOException("API call failed: ${response.code}"))
                }

                val responseBody = response.body?.string() ?: return Result.Error(IOException("Empty response"))
                val jsonResponse = JSONObject(responseBody)
                return Result.Success(
                    jsonResponse
                        .getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                )
            }
        } catch (e: IOException) {
            return Result.Error(e)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override fun requiresApiKey(): Boolean = true

    override fun getName(): String = "Gemini"

    override fun getDescription(): String = "Google's Gemini Pro - Advanced language model"

    override fun validateApiKey(apiKey: String): Boolean {
        this.apiKey = apiKey
        return apiKey.isNotEmpty()
    }
}
