package com.example.KBA_6_6.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.KBA_6_6.data.api.NobelApi
import com.example.KBA_6_6.data.repository.NobelRepositoryImpl
import com.example.KBA_6_6.domain.usecase.GetLaureatesUseCase
import com.example.KBA_6_6.presentation.list.LaureateListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NobelViewModel : ViewModel() {

	private val api = NobelApi.create()
	private val repository = NobelRepositoryImpl(api)
	private val getLaureatesUseCase = GetLaureatesUseCase(repository)

	private val _state = MutableStateFlow<LaureateListState>(LaureateListState.Loading)
	val state: StateFlow<LaureateListState> = _state.asStateFlow()

	private var currentYear: String? = null
	private var currentCategory: String? = null

	init {
		// Автоматический логин с тестовой учётной записью
		viewModelScope.launch {
			val success = api.login("admin", "password")
			if (success) {
				loadLaureates()
			} else {
				_state.value = LaureateListState.Error("Ошибка авторизации")
			}
		}
	}

	fun loadLaureates(year: String? = currentYear, category: String? = currentCategory) {
		currentYear = year
		currentCategory = category

		viewModelScope.launch {
			_state.value = LaureateListState.Loading
			try {
				val laureates = getLaureatesUseCase(year, category)
				if (laureates.isEmpty()) {
					_state.value = LaureateListState.Empty
				} else {
					_state.value = LaureateListState.Success(laureates)
				}
			} catch (e: Exception) {
				_state.value = LaureateListState.Error(e.message ?: "Unknown error")
			}
		}
	}
}