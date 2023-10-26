package com.example.photogalleryexample.retrofit

import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.Response

private const val API_KEY = "c947b05043c3db19a189cd18cb0dbeec"

/* Intercept a request and allow to manipulate with the contents*/
class PhotoInterceptor : okhttp3.Interceptor {
    override fun intercept(chain: okhttp3.Interceptor.Chain): Response {
        // get the request
        val originalRequest: Request = chain.request()

        // create custom url based on the original url
        val newUrl: HttpUrl = originalRequest.url.newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .addQueryParameter("format", "json")
            .addQueryParameter("nojsoncallback", "1")
            .addQueryParameter("extras", "url_s")
            .addQueryParameter("safesearch", "1")
            .build()

        // create custom request according created url
        val newRequest: Request = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}