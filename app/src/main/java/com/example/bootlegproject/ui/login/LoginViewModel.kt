package com.example.bootlegproject.ui.login

import android.util.Patterns
import androidx.lifecycle.*
import com.example.bootlegproject.data.AuthRepository
import com.example.bootlegproject.data.Result

import com.example.bootlegproject.R
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    /**
     * Login coroutine logic
     */
    fun login(email: String, password: String) {

        viewModelScope.launch{
            val loginJobCode = authRepository.loginJob(email, password)

            when (loginJobCode) {
                is Result.Success -> _loginResult.value =
                    LoginResult(success = LoggedInUserView(email = loginJobCode.data.email))
                else -> _loginResult.value = LoginResult(error = R.string.login_failed)
            }

        }

    }

    /**
     * Probably unnecessary for the login activity
     */
    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@") and username.isNotBlank()) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            false
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 4
    }


}
