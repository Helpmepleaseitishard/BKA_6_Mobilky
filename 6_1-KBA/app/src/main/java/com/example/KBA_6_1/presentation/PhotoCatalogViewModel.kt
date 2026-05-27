package com.example.KBA_6_1.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.KBA_6_1.data.repository.PhotoRepositoryImpl
import com.example.KBA_6_1.domain.usecase.GetPhotosUseCase
import com.example.a6_1.presentation.photoscreen.PhotoListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhotoCatalogViewModel : ViewModel() {

    private val repository = PhotoRepositoryImpl()
    private val getPhotosUseCase = GetPhotosUseCase(repository)

    private val _state = MutableStateFlow<PhotoListState>(PhotoListState.Loading)
    val state: StateFlow<PhotoListState> = _state.asStateFlow()

    init {
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            _state.value = PhotoListState.Loading
            try {
                val photos = getPhotosUseCase()
                _state.value = PhotoListState.Success(photos)
            } catch (e: Exception) {
                _state.value = PhotoListState.Error(e.message ?: "Unknown error")
            }
        }
    }
}