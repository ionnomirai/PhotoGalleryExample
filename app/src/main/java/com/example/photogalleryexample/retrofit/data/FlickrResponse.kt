package com.example.photogalleryexample.retrofit.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FlickrResponse(
    val photos: PhotoRespomse
)
