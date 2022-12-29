package com.catness.blinkblinkbeach.data.repositories.eventList

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.catness.blinkblinkbeach.utilities.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferencesManager"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.USER_PREFERENCES)

enum class SortOrder { BY_NAME, BY_DATE }

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    val preferencesFlow: Flow<SortOrder> = context.dataStore.data
        .catch { exception ->
            // handle error when something went wrong
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preference ->
            // valueOf function maps String enum into enum
            val sortOrder = SortOrder.valueOf(
                preference[PreferencesKeys.SORT_ORDER_KEY] ?: SortOrder.BY_DATE.name
            )
            sortOrder
        }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        context.dataStore.edit { preference ->
            preference[PreferencesKeys.SORT_ORDER_KEY] = sortOrder.name
        }
    }

    // create PreferencesKeys namespace to improve readability
    private object PreferencesKeys {
        val SORT_ORDER_KEY = stringPreferencesKey(Constants.SORT_ORDER)
    }
}