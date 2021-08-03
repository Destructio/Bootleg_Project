package com.example.bootlegproject.data

import com.example.bootlegproject.data.model.LoggedInUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        //TODO: Load saved user or null
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun loginJob(username: String, password: String): Result<LoggedInUser> {
        return withContext(Dispatchers.IO) {
            val result = dataSource.loginRequest(username, password)
            if (result is Result.Success) {
                setLoggedInUser(result.data)
            }
            result
        }
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        //TODO: Save user in local storage
    }
}