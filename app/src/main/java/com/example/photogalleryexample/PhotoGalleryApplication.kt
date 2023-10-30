package com.example.photogalleryexample

import android.app.Application
import com.example.photogalleryexample.repositories.PreferencesRepository

class PhotoGalleryApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        PreferencesRepository.initialise(this)
    }
}