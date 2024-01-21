package com.example.biletzone.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT

object Utility {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun createBaseURL(): String {
        return "${Constants.GATEWAY_SCHEMA}${Constants.GATEWAY_HOST}:${Constants.GATEWAY_PORT}"
    }

    fun getSubjectFromJwt(jwtToken: String): String? {
        try {
            val decodedJWT: DecodedJWT = JWT.decode(jwtToken)
            return decodedJWT.subject
        } catch (e: Exception) {
            // Handle exceptions, such as invalid JWT format or decoding issues
            e.printStackTrace()
        }
        return null
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}