package com.glebkrep.simplebudget.core.database.di

import android.content.Context
import androidx.room.Room
import com.glebkrep.simplebudget.core.database.SimpleBudgetRoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Singleton
    @Provides
    fun providesSimpleBudgetRoomDB(
        @ApplicationContext context: Context,
    ): SimpleBudgetRoomDB {
        return Room.databaseBuilder(
            context = context,
            klass = SimpleBudgetRoomDB::class.java,
            "simple_budget-db",
        ).build()
    }
}

