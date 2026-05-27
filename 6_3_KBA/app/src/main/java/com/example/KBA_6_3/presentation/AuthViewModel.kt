package com.example.KBA_6_3.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.KBA_6_3.data.api.AuthApi
import com.example.KBA_6_3.data.datastore.TokenManager
import com.example.KBA_6_3.data.repository.AuthRepositoryImpl
import com.example.KBA_6_3.domain.entity.User
import com.example.KBA_6_3.domain.usecase.GetUserDetailUseCase
import com.example.KBA_6_3.domain.usecase.GetUsersUseCase
import com.example.KBA_6_3.domain.usecase.LoginUseCase
import com.example.KBA_6_3.presentation.login.LoginState
import com.example.KBA_6_3.presentation.users.UsersListState
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class AuthViewModel(private val context: Context) : ViewModel() {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
            })
        }
        install(Logging) { level = LogLevel.INFO }
    }

    private val tokenManager = TokenManager(context)
    private val api = AuthApi(client)
    private val repository = AuthRepositoryImpl(api, tokenManager)

    private val loginUseCase = LoginUseCase(repository)
    private val getUsersUseCase = GetUsersUseCase(repository)
    private val getUserDetailUseCase = GetUserDetailUseCase(repository)

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _usersState = MutableStateFlow<UsersListState>(UsersListState.Loading)
    val usersState: StateFlow<UsersListState> = _usersState.asStateFlow()

    private val _userDetailState = MutableStateFlow<User?>(null)
    val userDetailState: StateFlow<User?> = _userDetailState.asStateFlow()

    init {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token != null) {
                loadUsers(token)
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val token = loginUseCase(username, password)
                _loginState.value = LoginState.Success(token)
                loadUsers(token)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun loadUsers(token: String) {
        viewModelScope.launch {
            _usersState.value = UsersListState.Loading
            try {
                val users = getUsersUseCase(token)
                _usersState.value = UsersListState.Success(users)
            } catch (e: Exception) {
                _usersState.value = UsersListState.Error(e.message ?: "Failed to load users")
            }
        }
    }

    fun loadUserDetail(userId: Int, token: String) {
        viewModelScope.launch {
            try {
                val user = getUserDetailUseCase(userId, token)
                _userDetailState.value = user
            } catch (e: Exception) {
                _userDetailState.value = null
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.clearToken()
            _loginState.value = LoginState.Idle
            _usersState.value = UsersListState.Loading
            _userDetailState.value = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        client.close()
    }
}