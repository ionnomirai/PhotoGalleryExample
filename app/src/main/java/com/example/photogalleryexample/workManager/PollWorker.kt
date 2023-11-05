package com.example.photogalleryexample.workManager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.photogalleryexample.repositories.PhotoRepository
import com.example.photogalleryexample.repositories.PreferencesRepository
import kotlinx.coroutines.flow.first

private const val TAG = "PollWorker_TAG"

class PollWorker (
    private val context: Context,
    workerParameters: WorkerParameters
        ) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        // get PreferenceRepository instance
        val preferencesRepository = PreferencesRepository.get()
        // create a PhotoRepository instance
        val photoRepository = PhotoRepository()

        // The query made by user (for example: cars, planets, Moon etc.)
        val query = preferencesRepository.storedQuery.first()
        // Last - because it is current result, not new.
        val lastId = preferencesRepository.lastResultId.first()

        Log.d(TAG, "This is query: $query")

        /* if query is empty, we no need to continue add some new,
        * because we don't know what need to add */
        if(query.isEmpty()){
            Log.i(TAG, "No saved query, finishing early.")
            return Result.success()
        }

        return try {
            // receive a new answer from Flickr with List<GalleryItem>
            // This new answer has the same parameters as before
            val items = photoRepository.searchPhotos(query)

            if(items.isNotEmpty()){
                //get first element id in the list the list of pictures
                val newResultId = items.first().id
                if (newResultId == lastId){
                    Log.i(TAG, "Still have the same result: $newResultId")
                }
                else{
                    Log.i(TAG, "Got a new result: $newResultId")
                    preferencesRepository.setLastResultId(newResultId)
                }
            }
            Result.success()
        } catch (ex: Exception){
            Log.e(TAG, "Background update failed", ex)
            Result.failure()
        }
    }
}