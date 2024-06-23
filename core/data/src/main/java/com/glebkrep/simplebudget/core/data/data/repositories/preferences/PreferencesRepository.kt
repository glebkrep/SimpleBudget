package com.glebkrep.simplebudget.core.data.data.repositories.preferences

import com.glebkrep.simplebudget.model.AppPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getPreferences(): Flow<AppPreferences>
    suspend fun setPreferences(appPreferences: AppPreferences)
}
