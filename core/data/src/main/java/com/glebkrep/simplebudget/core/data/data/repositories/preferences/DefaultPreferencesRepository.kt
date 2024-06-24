package com.glebkrep.simplebudget.core.data.data.repositories.preferences

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.datastore.PreferencesDataStoreDataSource
import com.glebkrep.simplebudget.model.AppPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultPreferencesRepository @Inject constructor(
    private val preferencesDataStoreDataSource: PreferencesDataStoreDataSource,
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : PreferencesRepository {
    override fun getPreferences(): Flow<AppPreferences> =
        preferencesDataStoreDataSource.getPreferences().flowOn(ioDispatcher)

    override suspend fun setPreferences(appPreferences: AppPreferences) {
        withContext(ioDispatcher) {
            preferencesDataStoreDataSource.setPreferences(
                appPreferences
            )
        }
    }
}
