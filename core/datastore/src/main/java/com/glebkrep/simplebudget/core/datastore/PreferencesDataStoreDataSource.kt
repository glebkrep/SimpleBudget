package com.glebkrep.simplebudget.core.datastore

import androidx.datastore.core.DataStore
import com.glebkrep.simplebudget.PreferencesProto
import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.model.AppPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesDataStoreDataSource @Inject constructor(
    private val preferencesDataDatastore: DataStore<PreferencesProto>,
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {

    fun getPreferences(): Flow<AppPreferences> =
        preferencesDataDatastore.data.map {
            return@map AppPreferences(
                isCommentsEnabled = it.areCommentsEnabled
            )
        }.flowOn(ioDispatcher)

    suspend fun setPreferences(appPreferences: AppPreferences) =
        withContext(ioDispatcher) {
            preferencesDataDatastore.updateData { prefs ->
                prefs.toBuilder().apply {
                    appPreferences.isCommentsEnabled?.let {
                        setAreCommentsEnabled(it)
                    }
                }.build()
            }
        }
}
