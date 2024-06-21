package com.glebkrep.simplebudget.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.glebkrep.simplebudget.BudgetDataProto
import com.glebkrep.simplebudget.PreferencesProto
import com.glebkrep.simplebudget.core.common.Dispatcher
import com.glebkrep.simplebudget.core.common.SimpleBudgetDispatcher
import com.glebkrep.simplebudget.core.common.di.ApplicationScope
import com.glebkrep.simplebudget.core.datastore.serializers.BudgetDataSerializer
import com.glebkrep.simplebudget.core.datastore.serializers.PreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    internal fun providesBudgetDataDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(SimpleBudgetDispatcher.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        budgetDataSerializer: BudgetDataSerializer,
    ): DataStore<BudgetDataProto> =
        DataStoreFactory.create(
            serializer = budgetDataSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            migrations = listOf(),
        ) {
            context.dataStoreFile("budgetdata.proto")
        }

    @Provides
    @Singleton
    internal fun providesPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(SimpleBudgetDispatcher.IO) ioDispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        preferencesSerializer: PreferencesSerializer,
    ): DataStore<PreferencesProto> =
        DataStoreFactory.create(
            serializer = preferencesSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            migrations = listOf(),
        ) {
            context.dataStoreFile("preferences.proto")
        }
}
