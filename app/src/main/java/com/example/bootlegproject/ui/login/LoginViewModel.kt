package com.example.bootlegproject.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.bootlegproject.data.LoginRepository
import com.example.bootlegproject.data.Result

import com.example.bootlegproject.R
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    /**
     * Login coroutine logic
     */
    fun login(email: String, password: String) {

        viewModelScope.launch{

            val loginJobCode = loginRepository.loginJob(email, password)

            when (loginJobCode) {
                is Result.Success -> _loginResult.value =
                    LoginResult(success = LoggedInUserView(displayName = loginJobCode.data.displayName))
                else -> _loginResult.value = LoginResult(error = R.string.login_failed)
            }

        }

    }

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
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 4
    }
}