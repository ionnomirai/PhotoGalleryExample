package com.example.photogalleryexample.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photogalleryexample.repositories.PhotoRepository
import com.example.photogalleryexample.retrofit.data.GalleryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "PhotoGalleryViewModel_TAG"

class PhotoGalleryViewModel : ViewModel() {

    private val photoRepository: PhotoRepository = PhotoRepository()
    private val _galleryItems: MutableStateFlow<List<GalleryItem>> = MutableStateFlow(emptyList())
    val galleryItems: StateFlow<List<GalleryItem>>
        get() = _galleryItems.asStateFlow()

    /*request data when the viewModel created. The request will only be made once
    * When viewModelScope is canceled, the network request will also be canceled*/
    init {
        try {
            viewModelScope.launch{
                val items = photoRepository.searchPhotos("planets")
                Log.d(TAG, "Items received: $items")
                _galleryItems.value = items
            }
        }
        catch (ex: Exception){
            Log.d(TAG, "Failed to fetch gallery items", ex)
        }
    }

    fun setQuery(query: String){
        viewModelScope.launch { _galleryItems.value = fetchGalleryItems(query) }
    }

    /*pull photo by query name from user, or default empty call otherwise */
    private suspend fun fetchGalleryItems(query: String): List<GalleryItem>{
        return if (query.isNotEmpty()){
            photoRepository.searchPhotos(query)
        }
        else{
            photoRepository.fetchPhotos()
        }
    }

}