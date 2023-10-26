package com.example.photogalleryexample.repositories

import com.example.photogalleryexample.retrofit.FlickrApi
import com.example.photogalleryexample.retrofit.PhotoInterceptor
import com.example.photogalleryexample.retrofit.data.GalleryItem
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
//import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

class PhotoRepository {
    // interface instance (completed setting in init block)
    private val flickrApi: FlickrApi

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(PhotoInterceptor())
            .build()

        // creating Retrofit instance
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.flickr.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()

        // creating object of FlickrApi interface using Retrofit object
        flickrApi = retrofit.create<FlickrApi>()
    }

    //public function for using GET function Flickr Home page  in another classes
    suspend fun fetchContents() = flickrApi.fetchContents()

    //get the list of photos
    suspend fun fetchPhotos(): List<GalleryItem> = flickrApi.fetchPhotos().photos.galleryItems

    // Find a photo by the text entered by the user
    suspend fun searchPhotos(query: String): List<GalleryItem> =
        flickrApi.searchPhotos(query).photos.galleryItems
}