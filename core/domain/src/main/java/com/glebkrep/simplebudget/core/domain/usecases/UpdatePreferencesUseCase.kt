package com.glebkrep.simplebudget.core.domain.usecases

import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.data.data.repositories.preferences.PreferencesRepository
import com.glebkrep.simplebudget.model.AppPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdatePreferencesUseCase @Inject internal constructor(
    private val preferencesRepository: PreferencesRepository,
    @Dispatcher(SimpleBudgetDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        appPreferences: AppPreferences,
    ) = withContext(ioDispatcher) {
        preferencesRepository.setPreferences(appPreferences)
    }
}
