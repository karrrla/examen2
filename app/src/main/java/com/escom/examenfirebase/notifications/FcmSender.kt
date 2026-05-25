package com.escom.examenfirebase.notifications

import android.content.Context
import com.escom.examenfirebase.utils.Constants
import com.google.auth.oauth2.GoogleCredentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayInputStream

/**
 * Envía notificaciones usando FCM HTTP v1 API.
 *
 * NOTA: Esta implementación incluye el JSON de service account directamente en la app,
 * lo cual es ACEPTABLE PARA UN EXAMEN/DEMO pero NO para producción
 * (cualquiera podría extraer las credenciales del APK).
 *
 * En producción, esto debería hacerse desde Cloud Functions.
 */
object FcmSender {

    private val client = OkHttpClient()
    private const val SCOPE = "https://www.googleapis.com/auth/firebase.messaging"

    private fun getAccessToken(): String? {
        return try {
            val json = Constants.SERVICE_ACCOUNT_JSON.trimIndent()
            val stream = ByteArrayInputStream(json.toByteArray())
            val credentials = GoogleCredentials.fromStream(stream)
                .createScoped(listOf(SCOPE))
            credentials.refreshIfExpired()
            credentials.accessToken.tokenValue
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun sendToToken(context: Context, token: String, title: String, body: String): Boolean {
        val accessToken = getAccessToken() ?: return false
        val projectId = Constants.FIREBASE_PROJECT_ID

        val payload = JSONObject().apply {
            put("message", JSONObject().apply {
                put("token", token)
                put("notification", JSONObject().apply {
                    put("title", title)
                    put("body", body)
                })
                put("android", JSONObject().apply {
                    put("priority", "HIGH")
                    put("notification", JSONObject().apply {
                        put("channel_id", "examen_notifications")
                    })
                })
                put("data", JSONObject().apply {
                    put("title", title)
                    put("body", body)
                })
            })
        }

        val request = Request.Builder()
            .url("https://fcm.googleapis.com/v1/projects/$projectId/messages:send")
            .addHeader("Authorization", "Bearer $accessToken")
            .addHeader("Content-Type", "application/json; UTF-8")
            .post(payload.toString().toRequestBody("application/json".toMediaType()))
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                response.isSuccessful
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
