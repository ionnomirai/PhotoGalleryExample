package com.example.photogalleryexample.repositories

import com.example.photogalleryexample.retrofit.FlickrApi
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

class PhotoRepository {
    // interface instance (completed setting in init block)
    private val flickrApi: FlickrApi

    init {
        // creating Retrofit instance
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.flickr.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        // creating object of FlickrApi interface using Retrofit object
        flickrApi = retrofit.create<FlickrApi>()
    }

    //public function for using GET function Flickr Home page  in another classes
    suspend fun fetchContents() = flickrApi.fetchContents()

    //get the list of photos
    suspend fun fetchPhotos() = flickrApi.fetchPhotos()
}