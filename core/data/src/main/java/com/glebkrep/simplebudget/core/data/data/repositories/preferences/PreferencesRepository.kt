package com.glebkrep.simplebudget.core.data.data.repositories.preferences

import com.glebkrep.simplebudget.model.AppPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun getPreferences(): Flow<AppPreferences>
    suspend fun setPreferences(appPreferences: AppPreferences)
}
