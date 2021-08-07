package com.example.bootlegproject.data

import android.util.Log
import com.example.bootlegproject.data.model.LoggedInUser
import java.io.IOException
import com.example.bootlegproject.utils.WEB_IP
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request



class NetDataSource {

    fun loginRequest(email: String, password: String): Result<LoggedInUser> {
        val username = email.subSequence(0,email.indexOf("@")).toString()

        val url = "http://$WEB_IP/login?email=$email&pass=$password"
        val httpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .build()

        return try {
            val response = httpClient.newCall(request).execute()
            val code = response.body().string()
            Log.d("-----------------", "$url ============= $code" )
            if(code.equals("true"))
                Result.Success(LoggedInUser(java.util.UUID.randomUUID().toString(), username))
            else
                Result.Error(Exception("Wrong login data"))

        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun regRequest(email: String, password: String): Result<LoggedInUser> {
        val username = email.subSequence(0,email.indexOf("@")).toString()

        val url = "http://$WEB_IP/reg?name=$username&email=$email&pass=$password"
        val httpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .build()

        return try {
            val response = httpClient.newCall(request).execute()
            val code = response.body().string()
            Log.d("-----------------", "$url ============= $code" )
            if(code.equals("1"))
                Result.Success(LoggedInUser(java.util.UUID.randomUUID().toString(), username))
            else
                Result.Error(Exception("Wrong reg data"))

        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun computersRequest(email: String): String {

        val url = "http://$WEB_IP/getComputers?email=$email"

        val httpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .build()

        return try {
            val response = httpClient.newCall(request).execute()
            val code = response.body().string()
            Log.d("-----------------", "$url ============= $code" )
            if(!code.equals(null))
                code
            else
                Exception("Wrong computers data").toString()

        } catch (e: Throwable) {
            IOException("Error getting computers", e).toString()
        }
    }
    fun computerStatisticRequest(computerName: String, email: String): String {

        val url = "http://$WEB_IP/getJSON?computer=$computerName&email=$email"

        val httpClient = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .build()

        return try {
            val response = httpClient.newCall(request).execute()
            val code = response.body().string()
            Log.d("-----------------", "$url ============= $code" )
            if(!code.equals(null))
                code
            else
                Exception("Wrong computer info data").toString()

        } catch (e: Throwable) {
            IOException("Error getting computer info", e).toString()
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}