package com.example.routes.dataStuff

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.getSystemService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SendJSONToApi {
    @Suppress("DEPRECATION")
    fun checkNetworkConnection(activity: Activity): Boolean{
        val statusMessage = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = statusMessage.activeNetworkInfo
        val isConnected: Boolean = networkInfo?.isConnected ?: false
        if (networkInfo != null && isConnected) {
            // TODO: i can change something here if connected
        } else {
            // TODO: i cand change something here if not connected
        }
        return isConnected
    }

    suspend fun httpPost(urlStr: String, json: JSONObject): String {
        val result = withContext(Dispatchers.IO){
            val url = URL(urlStr)

            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")

            setPostRequestContent(connection, json)
            connection.connect()
            connection.responseMessage + ""
        }
        return result
    }

    private fun setPostRequestContent(connection: HttpURLConnection, jsonObject: JSONObject){
        val outputStream = connection.outputStream
        val writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
        writer.write(jsonObject.toString())
        // TODO: remove log
        Log.e("myJson", jsonObject.toString())

        writer.flush()
        writer.close()
        outputStream.close()
    }
}