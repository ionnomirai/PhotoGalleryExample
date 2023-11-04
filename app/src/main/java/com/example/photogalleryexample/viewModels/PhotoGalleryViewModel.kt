package com.example.photogalleryexample.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photogalleryexample.repositories.PhotoRepository
import com.example.photogalleryexample.repositories.PreferencesRepository
import com.example.photogalleryexample.retrofit.data.GalleryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "PhotoGalleryViewModel_TAG"

class PhotoGalleryViewModel : ViewModel() {

    private val photoRepository: PhotoRepository = PhotoRepository()
    private val preferencesRepository = PreferencesRepository.get()

    private val _uiState: MutableStateFlow<PhotoGalleryUiState> =
        MutableStateFlow(PhotoGalleryUiState())
    val uiState: StateFlow<PhotoGalleryUiState>
        get() = _uiState.asStateFlow()

    /*request data when the viewModel created. The request will only be made once
    * When viewModelScope is canceled, the network request will also be canceled*/
    init {
        viewModelScope.launch {
            preferencesRepository.storedQuery.collectLatest { storedQuery ->
                try {
                    val items = photoRepository.searchPhotos(storedQuery)
                    Log.d(TAG, "Items received: $items")
                    _uiState.update { oldState ->
                        oldState.copy(
                            images = items,
                            query = storedQuery
                        )
                    }
                } catch (ex: Exception) {
                    Log.d(TAG, "Failed to fetch gallery items", ex)
                }
            }
        }
    }

    fun setQuery(query: String) {
        viewModelScope.launch { preferencesRepository.setStoredQuery(query) }
    }

    /*pull photo by query name from user, or default empty call otherwise */
    private suspend fun fetchGalleryItems(query: String): List<GalleryItem> {
        return if (query.isNotEmpty()) {
            photoRepository.searchPhotos(query)
        } else {
            photoRepository.fetchPhotos()
        }
    }
}

data class PhotoGalleryUiState(
    val images: List<GalleryItem> = listOf(),
    val query: String = ""
)