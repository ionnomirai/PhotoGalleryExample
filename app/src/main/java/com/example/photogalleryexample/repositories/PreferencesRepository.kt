package com.example.photogalleryexample.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PreferencesRepository private constructor(
    private val dataStore: DataStore<Preferences>
) {

    /* Get data from DataStore.
    * - distinctUntilChanged - Removes data repetition */
    val storedQuery: Flow<String> = dataStore.data.map { preferences ->
        preferences[SEARCH_QUERY_KEY] ?: ""
    }.distinctUntilChanged()

    // add new data
    suspend fun setStoredQuery(query: String){
        dataStore.edit {
            it[SEARCH_QUERY_KEY] = query
        }
    }

    // Companion object is needed to get access to keys from any place of the app
    companion object {
        // This is regular key
        private val SEARCH_QUERY_KEY = stringPreferencesKey("search_query")
        private var INSTANCE: PreferencesRepository? = null

        fun initialise(context: Context){
            if(INSTANCE == null){
                val dataStore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile("setting")
                }
                INSTANCE = PreferencesRepository(dataStore)
            }
        }

        fun get(): PreferencesRepository{
            return INSTANCE ?: throw IllegalStateException(
                "PreferencesRepository must be initialized"
            )
        }
    }
}