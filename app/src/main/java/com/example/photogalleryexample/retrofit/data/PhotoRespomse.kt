package com.example.photogalleryexample.retrofit.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoRespomse(
    @Json(name = "photo") val galleryItems: List<GalleryItem>
)
