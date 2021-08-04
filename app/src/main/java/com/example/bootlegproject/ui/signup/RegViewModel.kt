package com.example.bootlegproject.ui.signup

import android.util.Patterns
import androidx.lifecycle.*
import com.example.bootlegproject.R
import com.example.bootlegproject.data.NetDataSource
import com.example.bootlegproject.data.AuthRepository
import com.example.bootlegproject.data.Result
import com.example.bootlegproject.ui.login.LoggedInUserView
import com.example.bootlegproject.ui.login.LoginFormState
import com.example.bootlegproject.ui.login.LoginResult
import com.example.bootlegproject.ui.login.LoginViewModel
import kotlinx.coroutines.launch

class RegViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _regForm = MutableLiveData<LoginFormState>()
    val regFormState: LiveData<LoginFormState> = _regForm

    private val _regResult = MutableLiveData<LoginResult>()
    val regResult: LiveData<LoginResult> = _regResult

    /**
     * SignUp coroutine logic
     */
    fun reg(email: String, password: String) {

        viewModelScope.launch{

            val regJobCode = authRepository.signupJob(email, password)

            when (regJobCode) {
                is Result.Success -> _regResult.value =
                    LoginResult(success = LoggedInUserView(email = regJobCode.data.email))
                else -> _regResult.value = LoginResult(error = R.string.reg_failed)
            }

        }

    }

    fun regDataChanged(email: String, password: String) {
        if (!isEmailValid(email)) {
            _regForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _regForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _regForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return if (email.contains("@") and email.isNotBlank()) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 4
    }

}
