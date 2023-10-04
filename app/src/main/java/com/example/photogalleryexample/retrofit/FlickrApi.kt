package com.example.photogalleryexample.retrofit

import retrofit2.http.GET

interface FlickrApi {

    @GET("/") // get requeest
    suspend fun fetchContents(): String
}