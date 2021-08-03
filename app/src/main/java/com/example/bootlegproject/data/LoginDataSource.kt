package com.example.bootlegproject.data

import com.example.bootlegproject.data.model.LoggedInUser
import java.io.IOException
import com.example.bootlegproject.utils.WEB_IP
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

class LoginDataSource {

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

            if(code.equals("true"))
                Result.Success(LoggedInUser(java.util.UUID.randomUUID().toString(), username))
            else
                Result.Error(Exception("Wrong data"))

        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}