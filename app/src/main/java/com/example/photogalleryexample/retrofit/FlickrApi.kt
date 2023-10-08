package com.example.photogalleryexample.retrofit

import retrofit2.http.GET

private const val API_KEY = "c947b05043c3db19a189cd18cb0dbeec"

interface FlickrApi {

    @GET("/") // get requeest from home page
    suspend fun fetchContents(): String

    /*get the list of photos (and URL as a neccesary component)*/
    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=${API_KEY}" +
            "&extras=url_s")
    suspend fun fetchPhotos(): String
}