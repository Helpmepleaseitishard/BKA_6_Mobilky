package com.example.KBA_6_3.presentation.users

import com.example.KBA_6_3.domain.entity.User

sealed class UsersListState {
    object Loading : UsersListState()
    data class Success(val users: List<User>) : UsersListState()
    data class Error(val message: String) : UsersListState()
}