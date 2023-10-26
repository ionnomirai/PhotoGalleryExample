package com.example.photogalleryexample.retrofit

import com.example.photogalleryexample.retrofit.data.FlickrResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "c947b05043c3db19a189cd18cb0dbeec"

interface FlickrApi {

    @GET("/") // get requeest from home page
    suspend fun fetchContents(): String

    /*get the list of photos (and URL as a neccesary component)*/
    @GET("services/rest/?method=flickr.interestingness.getList")
    suspend fun fetchPhotos(): FlickrResponse

    /*@Query("text") - it is annotation that allows us to dynamically append a query
    * parameter to the URL.
    * "text" - it is a parameter name
    * - query: String - it is a value */
    @GET("services/rest?method=flickr.photos.search")
    suspend fun searchPhotos(@Query("text") query: String): FlickrResponse
}

//https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&api_key=yourApiKeyHere&format=json&nojsoncallback=1&extras=url_s
/*
"services/rest/?method=flickr.interestingness.getList" +
"&api_key=$API_KEY" +
"&format=json" +
"&nojsoncallback=1" +
"&extras=url_s"*/